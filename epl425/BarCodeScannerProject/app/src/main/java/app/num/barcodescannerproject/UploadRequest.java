package app.num.barcodescannerproject;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UploadRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://35.164.2.68/Upload.php";
    private Map<String, String> params;

    public UploadRequest(String image, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("image", image);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
