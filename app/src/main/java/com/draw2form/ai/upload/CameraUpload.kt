import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts


object ImageHandler {

    var imageUri: Uri? = null

    fun takePicture(activity: ComponentActivity, callback: (Uri) -> Unit) {
        val takePicture = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Image captured successfully
                // You can use the 'imageUri' to upload or display the image
                callback.invoke(imageUri!!)
            }
        }
        takePicture.launch(null)
    }

    fun pickImage(activity: ComponentActivity, param: (Any) -> Unit) {
        val pickImage = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            }
        }
        pickImage.launch("image/*")
    }
}








/*@Composable
fun CameraUpload() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    DisposableEffect(Unit) {
        // Initialize the ImageHandler when the composable is first committed
        ImageHandler.takePicture(context as ComponentActivity)

        // Specify the cleanup action when the composable is removed
        onDispose {
            // Optionally perform cleanup when the composable is removed
        }
    }

    // Button to launch the camera capture
    IconButton(
        onClick = {
            if (hasCamPermission) {
                ImageHandler.takePicture(context as ComponentActivity)
            }
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .size(50.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Capture Image",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Button to launch the gallery picker
    IconButton(
        onClick = { ImageHandler.pickImage(context as ComponentActivity) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .size(50.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "Upload Image",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}*/
