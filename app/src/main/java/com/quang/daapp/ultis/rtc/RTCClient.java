package com.quang.daapp.ultis.rtc;

import android.content.Context;
import android.util.Log;

import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class RTCClient {

    private final String LOCAL_TRACK_ID = "local_track";
    private final String LOCAL_STREAM_ID = "local_stream";


    private Context context;
    private PeerConnectionFactory peerConnectionFactory;
    private VideoCapturer videoCapturer;
    private VideoSource localVideoSource;
    public PeerConnection peerConnection;

    private EglBase rootEglBase = EglBase.create();

    public RTCClient(Context context, PeerConnection.Observer observer) {
        this.context = context;
        initializationOptions();
        peerConnectionFactory = buildPeerConnectionFactory();
        videoCapturer = createVideoCapture();
        localVideoSource =peerConnectionFactory.createVideoSource(false);
        peerConnection = buildPeerConnection(observer);
    }

    private  VideoCapturer createVideoCapture() {
        VideoCapturer videoCapturer;
        videoCapturer = createCameraCapture(new Camera1Enumerator(false));

        return videoCapturer;
    }

    private  VideoCapturer createCameraCapture(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // Trying to find a front facing camera!
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // We were not able to find a front cam. Look for other cameras
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private List<PeerConnection.IceServer> getIceServers() {
        PeerConnection.IceServer iceServer = PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer();
        List<PeerConnection.IceServer> servers = new ArrayList<>();
        servers.add(iceServer);
        return servers;
    }

    private void initializationOptions() {
        PeerConnectionFactory.InitializationOptions options = PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
                .createInitializationOptions();
        PeerConnectionFactory.initialize(options);
    }

    private PeerConnectionFactory buildPeerConnectionFactory() {
        PeerConnectionFactory.Options opt = new PeerConnectionFactory.Options();
        opt.disableEncryption = true;
        opt.disableNetworkMonitor = true;
        return PeerConnectionFactory.builder()
                .setVideoDecoderFactory(new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext()))
                .setVideoEncoderFactory(new DefaultVideoEncoderFactory(rootEglBase.getEglBaseContext(), true, true))
                .setOptions(opt)
                .createPeerConnectionFactory();
    }

    private PeerConnection buildPeerConnection(PeerConnection.Observer observer) {
        Log.e("ICE", "CREATE PEER");
        return peerConnectionFactory.createPeerConnection(getIceServers(),observer);
    }

    public void initSurfaceView(SurfaceViewRenderer view) {
        view.setMirror(true);
        view.setEnableHardwareScaler(true);
        view.init(rootEglBase.getEglBaseContext(), null);
    }


    public void startLocalVideoCapture(SurfaceViewRenderer view) {
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
        videoCapturer.initialize(surfaceTextureHelper,context, localVideoSource.getCapturerObserver());
        videoCapturer.startCapture(320, 240, 60);

        VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("100", localVideoSource);
        localVideoTrack.addSink(view);
        MediaStream localStream = peerConnectionFactory.createLocalMediaStream(LOCAL_STREAM_ID);
        localStream.addTrack(localVideoTrack);

        peerConnection.addStream(localStream);
    }

    public void switchCamera() {
        CameraVideoCapturer cameraVideoCapturer = (CameraVideoCapturer) videoCapturer;
        cameraVideoCapturer.switchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {

            }

            @Override
            public void onCameraSwitchError(String s) {

            }
        });

    }

    public void call(SdpObserver observer) {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo","true"));
        peerConnection.createOffer(observer,constraints);

    }

    public void answer(SdpObserver observer) {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo","true"));
        peerConnection.createAnswer(observer,constraints);
    }

    public void onRemoteSessionReceived(SessionDescription sessionDescription) {

        peerConnection.setRemoteDescription(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {

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
        },sessionDescription);

    }

    public void setLocalSessionDescription(SessionDescription sessionDescription) {
        peerConnection.setLocalDescription(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {

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
        }, sessionDescription);
    }

    public void addIceCandidate(IceCandidate iceCandidate) {
        peerConnection.addIceCandidate(iceCandidate);
    }


    public void endCall() {
        peerConnection.close();
        peerConnection = null;
    }
}
