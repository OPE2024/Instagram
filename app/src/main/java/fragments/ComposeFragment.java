 package fragments;

 import static android.app.Activity.RESULT_OK;

 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Environment;
 import android.provider.MediaStore;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.core.content.FileProvider;
 import androidx.fragment.app.Fragment;

 import com.onaopemipodimowo.instagram.FeedActivity;
 import com.onaopemipodimowo.instagram.LoginActivity;
 import com.onaopemipodimowo.instagram.Post;
 import com.onaopemipodimowo.instagram.R;
 import com.parse.ParseException;
 import com.parse.ParseFile;
 import com.parse.ParseUser;
 import com.parse.SaveCallback;

 import java.io.File;

 /**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private Button btnLogout;
     private File photoFile;
     private String photoFileName;


     // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    public void onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        etDescription = view.findViewById(R.id.etDescription);
        btnCaptureImage =  view.findViewById(R.id.btnCaptureImage);
        ivPostImage =  view.findViewById(R.id.ivPostImage);
        btnSubmit =  view.findViewById(R.id.btnSubmit);
        btnLogout =  view.findViewById(R.id.btnLogout);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,currentUser,photoFile);
                Intent submitIntent = new Intent(getContext(), FeedActivity.class);
                startActivity(submitIntent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(logoutIntent);

            }
        });
    }

     private void launchCamera() {
         // create Intent to take a picture and return control to the calling application
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         // Create a File reference for future access
         photoFile = getPhotoFileUri(photoFileName);

         // wrap File object into a content provider
         // required for API >= 24
         // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
         Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
         intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

         // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
         // So as long as the result is not null, it's safe to use the intent.
         if (intent.resolveActivity(getContext().getPackageManager()) != null) {
             // Start the image capture intent to take photo
             startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
         }}

     @Override
     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
             if (resultCode == RESULT_OK) {
                 Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                 ivPostImage.setImageBitmap(takenImage);
             } else {
                 Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
             }
         }
     }

     // Returns the File for a photo stored on disk given the fileName
     public File getPhotoFileUri(String fileName) {
         // Get safe storage directory for photos
         // Use `getExternalFilesDir` on Context to access package-specific directories.
         // This way, we don't need to request external read/write runtime permissions.
         File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

         // Create the storage directory if it does not exist
         if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
             Log.d(TAG, "failed to create directory");
         }

         // Return the file target for the photo based on filename
         return new File(mediaStorageDir.getPath() + File.separator + fileName);

     }


     private void savePost(String description, ParseUser currentUser, File photoFile) {
         Post post = new Post();
         post.setDescription(description);
         post.setImage(new ParseFile(this.photoFile));
         post.setUser(currentUser);
         post.saveInBackground(new SaveCallback() {
             @Override
             public void done(ParseException e) {
                 if (e != null){
                     Log.e(TAG,"Error while saving",e);
                     Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                 }
                 Log.i(TAG,"Post save was successfull");
                 etDescription.setText(" ");
                 ivPostImage.setImageResource(0);

             }
         });
     }
}