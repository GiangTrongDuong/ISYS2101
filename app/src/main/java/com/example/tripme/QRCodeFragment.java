package com.example.tripme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripme.databinding.FragmentQrCodeBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

public class QRCodeFragment extends Fragment {
    private FragmentQrCodeBinding binding;
    private TextView tvResult;
    private SurfaceView sv;
    private String intentData = "";
    private FirebaseAuth mAuth;
    private String role, tripID;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String scannedPhone = data.getStringExtra("SCAN_RESULT");
//                        System.out.println("&&&&&phone = " + scannedPhone);
                    Toast.makeText(getActivity(), "Participant " + scannedPhone + " marked as Arrived",
                            Toast.LENGTH_SHORT).show();
                    tvResult.setText("Arrived: " + scannedPhone);
                    FirebaseDatabase.getInstance().getReference().child("trip").
                            child(tripID).child("participant").child(scannedPhone).
                            child("participantRole").setValue("Present");

                }
            }
        }
    );

    public QRCodeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Variables
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);
        binding = FragmentQrCodeBinding.inflate(inflater, container, false);
        tripID = getActivity().getIntent().getExtras().getString("tripID");
        role = getActivity().getIntent().getExtras().getString("role");
        LinearLayout llParticipant = view.findViewById(R.id.llParticipant);
        LinearLayout llManager = view.findViewById(R.id.llManager);
        if(role.equals("Participant")){
            llParticipant.setVisibility(View.VISIBLE);
            llManager.setVisibility(View.GONE);

            TextView tvQRscanDescription = view.findViewById(R.id.tvQRscanDescription);
            ImageView ivQRcode = view.findViewById(R.id.yourQRcode);
            mAuth = FirebaseAuth.getInstance();
            String phone = mAuth.getCurrentUser().getPhoneNumber();

            //code here
            MultiFormatWriter mfw = new MultiFormatWriter();
            try{
                //suggested to be <350 to fit most screens
                Bitmap bitmap = encodeAsBitmap(phone, 300, 300);
                ivQRcode.setImageBitmap(bitmap);
            }
            catch (WriterException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            llManager.setVisibility(View.VISIBLE);
            llParticipant.setVisibility(View.GONE);

            tvResult = view.findViewById(R.id.tvResult);
            Button btnScanQR = view.findViewById(R.id.btnScanQR);

            btnScanQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentIntegrator ii = new IntentIntegrator(getActivity());
                    ii.setOrientationLocked(true);
                    ii.setPrompt("Scan a participant's QR");
                    ii.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    //launch an activity, collect it back in the array list
                    someActivityResultLauncher.launch(ii.createScanIntent());
                }
            });
        }
        return view;
    }


    //from https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    Bitmap encodeAsBitmap(String str, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, width, height);

        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}