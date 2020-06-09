package com.quang.daapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.ultis.CommonUltis;
import com.quang.daapp.ultis.RTCClient;

import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoCallFragment extends Fragment {

    SurfaceViewRenderer renderer;



    public VideoCallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUltis.checkCameraPermission(getContext(),getActivity());
        return inflater.inflate(R.layout.fragment_video_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PeerConnectionFactory.InitializationOptions options = PeerConnectionFactory.InitializationOptions.builder(getContext())
                .setEnableInternalTracer(true)
                .createInitializationOptions();
        PeerConnectionFactory.initialize(options);
        PeerConnectionFactory peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();
        EglBase rootEglBase = EglBase.create();

        VideoCapturer videoCaptureAndroid = RTCClient.createVideoCapture();


        //MediaConstraints constraints = new MediaConstraints();
        //create an AudioSource instance
//        AudioSource audioSource = peerConnectionFactory.createAudioSource(constraints);
//        AudioTrack localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);
        //Create a VideoSource instance
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCaptureAndroid.isScreencast());

        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
        videoCaptureAndroid.initialize(surfaceTextureHelper, getContext(), videoSource.getCapturerObserver());
        videoCaptureAndroid.startCapture(320, 240, 60);

        VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);
        SurfaceViewRenderer videoView =  view.findViewById(R.id.renderer);
        videoView.setMirror(true);
        videoView.setEnableHardwareScaler(true);
        videoView.init(rootEglBase.getEglBaseContext(), null);
        localVideoTrack.addSink(videoView);

    }




}
