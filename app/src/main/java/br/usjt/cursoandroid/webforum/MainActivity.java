package br.usjt.cursoandroid.webforum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import br.usjt.cursoandroid.webforum.entity.Cliente;


public class MainActivity extends Activity {

    private int id_cliente = 0;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton bt1 = (ImageButton)findViewById(R.id.imageButtonNovaMensagem);


        //TODO verificar aqui no bco de dados de o cliente já foi cadastrado caso positivo colocar o id dele em id_cliente


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id_cliente == 0) {
                    Intent intent = new Intent(MainActivity.this, AdicionarClienteActivity.class);
                    startActivityForResult(intent,1);
                }else{
                    // TODO chama a tela de mensagens (cliente já registrado)
                    //Intent intent2 = new Intent(MainActivity.this, MenssagemActivity.class);
                    //startActivity(intent2);
                    Toast.makeText(MainActivity.this,"Falta Implementar",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
                id_cliente = resultCode;
                cliente = (Cliente)data.getExtras().getSerializable("cliente");
        }
    }


}
