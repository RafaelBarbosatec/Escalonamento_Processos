package rafael.barbosa.escalonamento.util;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by dev on 04/07/17.
 */

public class Animacoes {

    public static void startAnimationFadeIn(View view,long delay){
        view.setAlpha(0f);
        ViewPropertyAnimator animator2 = view.animate();
        float animationValue2 = view.getAlpha() == 0f ? 1f : 0f;
        animator2.alpha(animationValue2);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setStartDelay(delay);
        animator2.start();
    }

    public static void startAnimationSplash(View view, long delay){
        view.setAlpha(0f);
        view.setTranslationY(50f);
        ViewPropertyAnimator animator2 = view.animate();
        float animationValue2 = view.getAlpha() == 0f ? 1f : 0f;
        animator2.alpha(animationValue2);
        animator2.translationY(0f);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setStartDelay(delay);
        animator2.start();
    }

}
