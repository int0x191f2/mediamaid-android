package int0x191f2.mediamaid;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ip4gjb on 7/28/15.
 */
public class FilterListDialogFragment extends DialogFragment {
    EditText input;
    Button submitButton;
    String title;
    public interface FilterListDialogListener{
        void onFinishInputDialog(String list);
    }

    public FilterListDialogFragment(){
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.filter_list_input_fragment,container);
        final SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("MediaMaid", 0);
        input = (EditText) view.findViewById(R.id.filterListInput);
        submitButton = (Button) view.findViewById(R.id.filterListSubmit);
        input.setText(prefs.getString(BuildVars.SHARED_PREFERENCES_FILTER_LIST_KEY,""));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putString(BuildVars.SHARED_PREFERENCES_FILTER_LIST_KEY,input.getText().toString()).commit();
                Toast.makeText(getActivity().getApplicationContext(),"Filter list saved",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        input.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(this.title);

        return view;
    }
}
