package com.quang.daapp.ui.communication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ultis.WebSocketClient;
import com.quang.daapp.ultis.rtc.RTCClient;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;


/**
 * A simple {@link Fragment} subcflass.
 */
public class VideoCallFragment extends Fragment {

    private SurfaceViewRenderer localView;
    private SurfaceViewRenderer remoteView;
    private RTCClient rtcClient;
    private boolean isAnswered = false;
    private NavController navController;
    private Bundle bundle;
    private boolean isExpert;
    private int countConnect = 0;

    public VideoCallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int channel = getArguments().getInt("channel");
        isExpert = getArguments().getBoolean(getString(R.string.isExpert));
        boolean answer = getArguments().getBoolean("answer");
        bundle = new Bundle();
        bundle.putInt(getString(R.string.key_request_id), channel);
        bundle.putBoolean(getString(R.string.isExpert), isExpert);
        bundle.putBoolean("answer", false);
        localView=  view.findViewById(R.id.localRenderer);
        remoteView = view.findViewById(R.id.renderer);


        navController = Navigation.findNavController(view);

        view.findViewById(R.id.btnEndCall).setOnClickListener(v -> {
            back();
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            back();
        });

        rtcClient = new RTCClient(getContext(), new PeerConnection.Observer() {
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            }
            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                Log.e("ICE:","iceConnectionState: " + iceConnectionState.toString());
                switch (iceConnectionState) {
                    case CONNECTED: {
                        isAnswered = true;
                        break;
                    }

                    case DISCONNECTED:
                    case CLOSED: {
                        back();
                        break;
                    }
                }
            }

            @Override
            public void onIceConnectionReceivingChange(boolean b) {
                Log.e("ICE:","onIceConnectionReceivingChange");
            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                rtcClient.addIceCandidate(iceCandidate);
                sendInfo(iceCandidate,MessageType.ICE,channel);
            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
                Log.e("ICE:","onIceGatheringChange: " + iceCandidates.toString());
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                Log.e("ICE:","STREAM " + mediaStream.videoTracks.size());
                mediaStream.videoTracks.get(0).addSink(remoteView);

            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {
                Log.e("Obser:","onRemoveStream");
            }

            @Override
            public void onDataChannel(DataChannel dataChannel) {

            }

            @Override
            public void onRenegotiationNeeded() {

            }

            @Override
            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

            }
        });
        rtcClient.initSurfaceView(localView);
        rtcClient.initSurfaceView(remoteView);
        rtcClient.startLocalVideoCapture(localView);

        WebSocketClient.getInstance().getSubscribeChannelData(channel).observe(getViewLifecycleOwner(), new Observer<ReceiveMessage>() {
            @Override
            public void onChanged(ReceiveMessage message) {
                if(isExpert != message.isExpert()) {
                    switch (message.getType()) {
                        case ACCEPT: {
                            rtcClient.call(new SdpObserver() {
                                @Override
                                public void onCreateSuccess(SessionDescription sessionDescription) {
                                    rtcClient.setLocalSessionDescription(sessionDescription);
                                    sendInfo(sessionDescription,MessageType.OFFER,channel);
                                }

                                @Override
                                public void onSetSuccess() {

                                }

                                @Override
                                public void onCreateFailure(String s) {

                                }

                                @Override
                                public void onSetFailure(String s) {

                                }
                            });
                            break;
                        }
                        case OFFER: {
                            rtcClient.onRemoteSessionReceived(NetworkClient.getGson().fromJson(message.getMessage(),SessionDescription.class));
                            if(rtcClient.peerConnection.getLocalDescription() != null) {
                                sendInfo(rtcClient.peerConnection.getLocalDescription(),MessageType.ANSWER,channel);
                            }
                            rtcClient.answer(new SdpObserver() {
                                @Override
                                public void onCreateSuccess(SessionDescription sessionDescription) {
                                    sendInfo(sessionDescription,MessageType.ANSWER,channel);
                                    rtcClient.setLocalSessionDescription(sessionDescription);

                                }

                                @Override
                                public void onSetSuccess() {

                                }

                                @Override
                                public void onCreateFailure(String s) {

                                }

                                @Override
                                public void onSetFailure(String s) {

                                }
                            });
                            break;
                        }

                        case ANSWER: {
                            rtcClient.onRemoteSessionReceived(NetworkClient.getGson().fromJson(message.getMessage(),SessionDescription.class));
                            break;
                        }
                        case ICE: {
                            rtcClient.addIceCandidate(NetworkClient.getGson().fromJson(message.getMessage(),IceCandidate.class));
                            break;
                        }
                    }
                }

            }
        });

        if(answer) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(3000);
                            if(isAnswered) return;
                            SendMessage sendMessage = new SendMessage("",MessageType.ACCEPT);
                            WebSocketClient.getInstance().chat(channel,sendMessage);
                            countConnect++;
                            if(countConnect > 3) {
                                back();
                            }
                            Log.e("ICE", "THREAD " +countConnect );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
            thread.start();

        }else{
            SendMessage sendMessage = new SendMessage("" + channel,MessageType.CALLING);
            WebSocketClient.getInstance().chat(channel,sendMessage);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 12);
                        if(!isAnswered) {
                            back();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(!isAnswered) {
            isAnswered = true;
        }
        back();
        Log.e("ICE", "CLOSE");
        if(rtcClient.peerConnection != null) {
            rtcClient.endCall();
        }

    }

    private void sendInfo(Object object, MessageType messageType, int channel) {
        String json = NetworkClient.getGson().toJson(object);
        SendMessage sendMessage = new SendMessage(json, messageType);
        WebSocketClient.getInstance().chat(channel,sendMessage);
    }



    private void back() {
        if(isExpert) {
            navController.navigate(R.id.communicationFragment2,bundle);
        }else {
            navController.navigate(R.id.customerCommunicationFragment,bundle);
        }
    }



}
