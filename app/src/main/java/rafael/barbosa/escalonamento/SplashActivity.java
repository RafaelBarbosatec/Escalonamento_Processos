package rafael.barbosa.escalonamento;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import rafael.barbosa.escalonamento.Cadastro.CadastroActivity;
import rafael.barbosa.escalonamento.util.Animacoes;

public class SplashActivity extends AppCompatActivity {

    private ImageView img_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img_splash = (ImageView) findViewById(R.id.img_splash);

        img_splash.setAlpha(0f);
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long l) {
                Animacoes.startAnimationSplash(img_splash,100);
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, CadastroActivity.class));
                finish();
            }
        }.start();

    }
}
