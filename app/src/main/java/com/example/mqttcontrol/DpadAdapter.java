package com.example.mqttcontrol;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mqttcontrol.models.Dpad;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DpadAdapter extends RecyclerView.Adapter<DpadAdapter.ControllerHolder> {

    private ArrayList<String> localDataSet;

    public DpadAdapter(ArrayList<String> dataSet) {
        this.localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ControllerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller, parent, false);
        ControllerHolder holder = new ControllerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ControllerHolder holder, int position) {
        holder.controllerID.setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return this.localDataSet.size();
    }

    public static class ControllerHolder extends RecyclerView.ViewHolder{

        // TODO: Declare dpad variables here.

        private Dpad dpad;

        private EditText editIpAddress;
        private EditText editTopic;
        private Button buttonConnect;
        private TextView controllerID;

        private ImageButton Up;
        private ImageButton Left;
        private ImageButton Right;
        private ImageButton Down;

        private MqttAndroidClient client;

        private IMqttToken token;

        public ControllerHolder(@NonNull View itemView) {
            super(itemView);

            dpad = new Dpad();

            controllerID = itemView.findViewById(R.id.controllerID);
            editIpAddress = itemView.findViewById(R.id.editIpAddress);
            editTopic = itemView.findViewById(R.id.editTopic);
            buttonConnect = itemView.findViewById(R.id.buttonConnect);

            setEnabled();

            buttonConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connect(view);
                }
            });

            Up = itemView.findViewById(R.id.up);
            Left = itemView.findViewById(R.id.left);
            Right = itemView.findViewById(R.id.right);
            Down = itemView.findViewById(R.id.down);

            Up.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            dpad.setUp();
                            sendMessage();
                            break;
                        case MotionEvent.ACTION_UP:
                            dpad.resetState();
                            sendMessage();
                            break;
                    }
                    return false;
                }
            });

            Down.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            dpad.setDown();
                            sendMessage();
                            break;
                        case MotionEvent.ACTION_UP:
                            dpad.resetState();
                            sendMessage();
                            break;
                    }
                    return false;
                }
            });

            Left.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            dpad.setLeft();
                            sendMessage();
                            break;
                        case MotionEvent.ACTION_UP:
                            dpad.resetState();
                            sendMessage();
                            break;
                    }
                    return false;
                }
            });

            Right.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            dpad.setRight();
                            sendMessage();
                            break;
                        case MotionEvent.ACTION_UP:
                            dpad.resetState();
                            sendMessage();
                            break;
                    }
                    return false;
                }
            });

        }

        private void connect(View ItemvView){
            try{
                setDisabled();
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName("controller");
                options.setPassword("".toCharArray());

                // UUID.randomUUID().toString().replaceAll("-", "")
                client = new MqttAndroidClient(itemView.getContext(),"tcp://" +  editIpAddress.getText().toString(), controllerID.getText().toString());
                IMqttToken thisToken = client.connect();
                thisToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        setDisabled();
                        System.out.println("Connected");
                        Toast.makeText(itemView.getContext(), "Se ha conectado!", Toast.LENGTH_LONG).show();
                        token = thisToken;
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        setEnabled();
                        System.out.println("Failed");
                        exception.printStackTrace();
                        Toast.makeText(itemView.getContext(), "No se ha conectado!", Toast.LENGTH_LONG).show();
                    }
                });

            }catch (Exception e){
                setEnabled();
                e.printStackTrace();
            }

        }

        private boolean isConnected(){
            boolean isConnected = client.isConnected();
            if(!isConnected){
                buttonConnect.setEnabled(true);
            }
            return isConnected;
        }

        private void setEnabled(){
            this.buttonConnect.setEnabled(true);
            this.editIpAddress.setEnabled(true);
            this.editTopic.setEnabled(true);
        }

        private void setDisabled(){
            this.buttonConnect.setEnabled(false);
            this.editTopic.setEnabled(false);
            this.editIpAddress.setEnabled(false);
        }

        private void sendMessage(){
            String topic = "tmst/move";
            String message = Integer.toString(dpad.getState());

            if (token != null){
                if(isConnected()){
                    try{
                        client.publish(topic, message.getBytes(StandardCharsets.UTF_8), 0 , false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            dpad.resetState();
        }
    }


}
