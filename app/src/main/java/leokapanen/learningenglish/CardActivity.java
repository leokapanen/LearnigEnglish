package leokapanen.learningenglish;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import datamodel.DicRecord;
import db.DicDB;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class CardActivity extends AppCompatActivity implements View.OnDragListener {

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.l_sentence)
    TextView lSentence;

    @Bind(R.id.r_sentence)
    TextView rSentence;

    @Bind(R.id.l_translation)
    TextView lTranslation;

    @Bind(R.id.r_translation)
    TextView rTranslation;

    // Background constants
    private final int UNSELECTED_BG = R.drawable.grey_card_bg;
    private final int SELECTED_BG = R.drawable.selected_card_bg;
    private final int RED_BG = R.drawable.red_card_bg;
    private final int GREEN_BG = R.drawable.green_card_bg;

    // key for saved instance state bundle
    private final String KEY_CURRENT_STEP = "KEY_CURRENT_STEP";

    private List<DicRecord> records;
    private int step; // current step
    private boolean translationOrder; // order of translation words: true - direct, false - reverse

    // null == nothing was selected
    // true == left sentence was selected
    // false == right sentence was selected
    private Boolean selectedLSentence = null;

    // null == nothing was selected
    // true == left translation was selected
    // false == right translation was selected
    private Boolean selectedLTranslation = null;

    // null == nothing is dragging
    // true == left sentence is dragging
    // false == right sentence is dragging
    private Boolean draggingLSentence = null;

    // null == nothing is dragging
    // true == left translation is dragging
    // false == right translation is dragging
    private Boolean draggingLTranslation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            int curStep = savedInstanceState.getInt(KEY_CURRENT_STEP, -1);
            if (curStep != -1) {
                step = curStep;
            }
        }

        records = (List<DicRecord>) getIntent().getSerializableExtra(Conf.KEY_DIC_RECORDS);

        if ((records.size() > 0) && (records.size() % 2) != 0) {
            records.remove(records.size() - 1);
        }

        progressBar.setMax(records.size() / 2);

        lSentence.setOnDragListener(this);
        rSentence.setOnDragListener(this);
        lTranslation.setOnDragListener(this);
        rTranslation.setOnDragListener(this);

        nextStep();
    }

    private void nextStep() {
        if (step == (records.size() / 2)) {
            finish();
            return;
        }

        DicRecord lDicRecord = records.get(step * 2);
        DicRecord rDicRecord = records.get((step * 2) + 1);

        selectedLSentence = null;
        selectedLTranslation = null;

        lSentence.setBackgroundResource(UNSELECTED_BG);
        rSentence.setBackgroundResource(UNSELECTED_BG);
        lTranslation.setBackgroundResource(UNSELECTED_BG);
        rTranslation.setBackgroundResource(UNSELECTED_BG);
        lSentence.setEnabled(true);
        rSentence.setEnabled(true);

        lSentence.setText(lDicRecord.getSentence());
        rSentence.setText(rDicRecord.getSentence());

        translationOrder = (Math.random() > 0.5) ? true : false;

        if (translationOrder) {
            lTranslation.setText(lDicRecord.getTranslation());
            rTranslation.setText(rDicRecord.getTranslation());
        } else {
            lTranslation.setText(rDicRecord.getTranslation());
            rTranslation.setText(lDicRecord.getTranslation());
        }

        progressBar.setProgress(step);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_STEP, step);
        super.onSaveInstanceState(outState);
    }

    /**
     * Implements the logic to check the correctness of the choice cards
     */
    private void checkAndUpdateCardState() {
        if ((selectedLSentence != null) && (selectedLTranslation != null)) {

            if ((translationOrder && (selectedLSentence == selectedLTranslation)) ||
                    (!translationOrder && (selectedLSentence != selectedLTranslation))) {
                if (selectedLSentence) {
                    lSentence.setBackgroundResource(GREEN_BG);
                    lSentence.setEnabled(false);
                    DicRecord lDicRecord = records.get(step * 2);
                    lDicRecord.setStudied(true);
                    DicDB.INSTANCE.update(lDicRecord);
                } else {
                    rSentence.setBackgroundResource(GREEN_BG);
                    rSentence.setEnabled(false);
                    DicRecord rDicRecord = records.get((step * 2) + 1);
                    rDicRecord.setStudied(true);
                    DicDB.INSTANCE.update(rDicRecord);
                }
            } else {
                if (selectedLSentence) {
                    lSentence.setBackgroundResource(RED_BG);
                    lSentence.setEnabled(false);
                } else {
                    rSentence.setBackgroundResource(RED_BG);
                    rSentence.setEnabled(false);
                }
            }

            clearSelection();

            if (!lSentence.isEnabled() && !rSentence.isEnabled()) {
                step++;
                nextStep();
            }
        }

    }

    private void clearSelection() {
        if (lSentence.isEnabled()) {
            lSentence.setBackgroundResource(UNSELECTED_BG);
        }
        if (rSentence.isEnabled()) {
            rSentence.setBackgroundResource(UNSELECTED_BG);
        }
        selectedLSentence = null;
        selectedLTranslation = null;
        lTranslation.setBackgroundResource(UNSELECTED_BG);
        rTranslation.setBackgroundResource(UNSELECTED_BG);
    }

    // Listeners

    @OnClick(R.id.stop_btn)
    public void onClickStop() {
        finish();
    }

    @OnClick(R.id.l_sentence)
    public void onLSentenceClick() {
        if (lSentence.isEnabled()) {
            selectedLSentence = true;
            lSentence.setBackgroundResource(SELECTED_BG);
        }
        if (rSentence.isEnabled()) {
            rSentence.setBackgroundResource(UNSELECTED_BG);
        }
        checkAndUpdateCardState();
    }

    @OnClick(R.id.r_sentence)
    public void onRSentenceClick() {
        if (rSentence.isEnabled()) {
            selectedLSentence = false;
            rSentence.setBackgroundResource(SELECTED_BG);
        }
        if (lSentence.isEnabled()) {
            lSentence.setBackgroundResource(UNSELECTED_BG);
        }
        checkAndUpdateCardState();
    }

    @OnClick(R.id.l_translation)
    public void onLTranslationClick() {
        selectedLTranslation = true;
        lTranslation.setBackgroundResource(SELECTED_BG);
        rTranslation.setBackgroundResource(UNSELECTED_BG);
        checkAndUpdateCardState();
    }

    @OnClick(R.id.r_translation)
    public void onRTranslationClick() {
        selectedLTranslation = false;
        rTranslation.setBackgroundResource(SELECTED_BG);
        lTranslation.setBackgroundResource(UNSELECTED_BG);
        checkAndUpdateCardState();
    }

    @OnLongClick(R.id.l_sentence)
    public boolean onLongClickLSentence(View v) {
        if (!v.isEnabled()) {
            return false;
        }

        draggingLSentence = true;
        startDrag(v);

        return false;
    }

    @OnLongClick(R.id.r_sentence)
    public boolean onLongClickRSentence(View v) {
        if (!v.isEnabled()) {
            return false;
        }

        draggingLSentence = false;
        startDrag(v);

        return false;
    }

    @OnLongClick(R.id.l_translation)
    public boolean onLongClickLTranslation(View v) {
        draggingLTranslation = true;
        startDrag(v);

        return false;
    }

    @OnLongClick(R.id.r_translation)
    public boolean onLongClickRTranslation(View v) {
        draggingLTranslation = false;
        startDrag(v);

        return false;
    }

    private void startDrag(View v) {
        clearSelection();

        ClipData dragData = ClipData.newPlainText("text", "translation");
        v.startDrag(
                dragData,
                new View.DragShadowBuilder(v),
                v,
                0
        );
    }

    @Override
    public boolean onDrag(View targetView, DragEvent event) {
        int action = event.getAction();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED: {
                return true;
            }
            case DragEvent.ACTION_DRAG_LOCATION: {
                return true;
            }
            case DragEvent.ACTION_DRAG_ENTERED: {
                return true;
            }
            case DragEvent.ACTION_DROP: {
                if (draggingLSentence != null) {

                    if (targetView.getId() == lTranslation.getId()) {
                        selectedLTranslation = true;
                    }

                    if (targetView.getId() == rTranslation.getId()) {
                        selectedLTranslation = false;
                    }

                    if (selectedLTranslation != null) {
                        selectedLSentence = draggingLSentence;
                    }
                }

                if (draggingLTranslation != null) {
                    if ((targetView.isEnabled()) && (targetView.getId() == lSentence.getId())) {
                        selectedLSentence = true;
                    }

                    if ((targetView.isEnabled()) && (targetView.getId() == rSentence.getId())) {
                        selectedLSentence = false;
                    }

                    if (selectedLSentence != null) {
                        selectedLTranslation = draggingLTranslation;
                    }
                }

                targetView.invalidate();
                draggingLSentence = null;
                draggingLTranslation = null;

                checkAndUpdateCardState();

                return true;
            }
            default: {
                return false;
            }
        }
    }

}
