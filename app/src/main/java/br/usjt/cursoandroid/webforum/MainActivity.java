package br.usjt.cursoandroid.webforum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.usjt.cursoandroid.webforum.entity.Cliente;


public class MainActivity extends Activity {

    private int id_cliente = 0;
    private Cliente cliente;
    private static final String FILE_PATH = "/sdcard/save_object.bin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isFileExsist()){
            cliente = (Cliente)loadSerializedObject();
        }else{
            cliente = new Cliente();
            cliente.setId(0);
        }


        ImageButton bt1 = (ImageButton)findViewById(R.id.imageButtonNovaMensagem);


        //TODO verificar aqui no bco de dados de o cliente já foi cadastrado caso positivo colocar o id dele em id_cliente


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cliente.getId() == 0) {
                    Intent intent = new Intent(MainActivity.this, AdicionarClienteActivity.class);
                    startActivityForResult(intent,1);
                }else{
                    // TODO chama a tela de mensagens (cliente já registrado)
                    Intent intent2 = new Intent(MainActivity.this, MensagemActivity.class);
                    intent2.putExtra("cliente",cliente);
                    startActivity(intent2);
                    //Toast.makeText(MainActivity.this,"Falta Implementar",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
                id_cliente = resultCode;
                cliente = (Cliente)data.getExtras().getSerializable("cliente");
                saveObject(cliente);
        }
    }

    public void saveObject(Cliente c){
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FILE_PATH))); //Select where you wish to save the file...
            oos.writeObject(c); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex)
        {
            Log.v("Serialization Save Error : ",ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Object loadSerializedObject()
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            Log.v("Serialization Read Error : ", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public Boolean isFileExsist() {

            File file = new File(FILE_PATH);
            return file.exists();
    }

}
