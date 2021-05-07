package com.example.myshools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MineqqqFragment extends Fragment {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate( R.layout.mine_fragment, container, false );
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

      /*  textView1=getActivity().findViewById(R.id.cankao);
        textView2=getActivity().findViewById(R.id.cankao1);
        textView3=getActivity().findViewById(R.id.cankao2);
        textView4=getActivity().findViewById(R.id.cankao3);
        setAdapter();
*/



    }

    /*private void setAdapter() {
        MyListener my=new MyListener();
        textView1.setOnClickListener(my);
        textView2.setOnClickListener(my);
        textView3.setOnClickListener(my);
        textView4.setOnClickListener(my);
    }

    private class MyListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.cankao:
                    Intent intent=new Intent(getContext(), FeedBackActivity.class);
                    startActivity(intent);


                    break;
                case R.id.cankao1:
                    Intent intent1=new Intent(getContext(), FunctionIntroductionActivity.class);
                    startActivity(intent1);

                    break;

                case R.id.cankao2:
                    Intent intent2=new Intent(getContext(), PrivacyAgreementActivity.class);
                    startActivity(intent2);
                    break;


            }

        }*/
  //  }
}
