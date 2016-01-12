package int0x191f2.mediamaid;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchUserDialogFragment extends DialogFragment {
    EditText input;
    Button submitButton;
    String title;
    public interface SearchUserDialogListener{
        void onFinishInputDialog(String list);
    }

    public SearchUserDialogFragment(){
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.search_user_dialog_frament_layout,container);
        input = (EditText) view.findViewById(R.id.twitterUsernameInput);
        submitButton = (Button) view.findViewById(R.id.twitterUsernameSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),TwitterProfileViewActivity.class);
                intent.putExtra("id",input.getText());
                v.getContext().startActivity(intent);
                dismiss();
            }
        });
        input.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(this.title);

        return view;
    }
}
