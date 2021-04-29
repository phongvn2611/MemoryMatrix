package com.example.memorymatrix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int[] idArray = {R.id.ibt1, R.id.ibt2, R.id.ibt3, R.id.ibt4, R.id.ibt5, R.id.ibt6,
            R.id.ibt7, R.id.ibt8, R.id.ibt9, R.id.ibt10, R.id.ibt11, R.id.ibt12,
            R.id.ibt13, R.id.ibt14, R.id.ibt15, R.id.ibt16, R.id.ibt17, R.id.ibt18,
            R.id.ibt19, R.id.ibt20, R.id.ibt21, R.id.ibt22, R.id.ibt23, R.id.ibt24,
            R.id.ibt25, R.id.ibt26, R.id.ibt27, R.id.ibt28, R.id.ibt29, R.id.ibt30,
            R.id.ibt31, R.id.ibt32, R.id.ibt33, R.id.ibt34, R.id.ibt35, R.id.ibt36};

    int numberOfTargetTiles = 3;
    ImageButton[] imageButtons = new ImageButton[idArray.length];
    int[] targetTiles;
    TextView txtScore, txtLogo, txtFinalScore, txtTiles;
    Button btnStart;
    CountDownTimer cdtStart, cdtSplash, cdtFinish;
    ImageButton ibtPlay, ibtPause;
    ConstraintLayout ctlInfo, ctlBoard;
    Random rd;
    int currentScore = 0;
    int scorePerTile = 250;
    int bonusScore = 100;
    int numberOfRightTiles = 0;
    int numberOfRightAnswer = 0;
    int numberOfWrongAnswer = 0;
    int targetValue;
    boolean isClick;
    boolean[] isSelected = new boolean[idArray.length];

    private void ResetTilesBoard() {
        numberOfRightTiles = 0;
        for (int i = 0; i < idArray.length; i++) {
            imageButtons[i].setBackgroundResource(0);
            imageButtons[i].setBackgroundColor(Color.rgb(113, 74, 67));
        }
        for (int i = 0; i < targetTiles.length; i++) {
            targetTiles[i] = -1;
        }
    }

    private boolean CheckRandomTile(int value) {
        for (int i = 0; i < targetTiles.length; i++) {
            if (value == targetTiles[i])
                return false;
        }
        return true;
    }

    private void ShowAllTargetTiles() {
        for (int i = 0; i < targetTiles.length; i++) {
            imageButtons[targetTiles[i]].setBackgroundColor(Color.rgb(74, 179, 184));
        }
    }

    private void InitSelected() {
        for (int i = 0; i < isSelected.length; i++) {
            isSelected[i] = false;
        }
    }

    private void StartLevel() {
        txtTiles.setText("TILES  " + numberOfTargetTiles);
        targetTiles = new int[numberOfTargetTiles];
        InitSelected();
        ibtPause.setEnabled(false);
        cdtStart = new CountDownTimer(1500, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                isClick = false;
                ResetTilesBoard();
            }

            @Override
            public void onFinish() {
                cdtStart.cancel();
                SplashTiles();
            }
        }.start();
    }

    private void SplashTiles() {
        cdtSplash = new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isClick = false;
                for (int i = 0; i < numberOfTargetTiles; i++) {
                    targetValue = rd.nextInt(36);
                    if (CheckRandomTile(targetValue) == true) {
                        targetTiles[i] = targetValue;
                        imageButtons[targetValue].setBackgroundColor(Color.rgb(74, 179, 184));
                    }
                    else {
                        targetValue = rd.nextInt(36);
                        i--;
                    }
                }
            }

            @Override
            public void onFinish() {
                for (int i = 0; i < numberOfTargetTiles; i++) {
                    imageButtons[targetTiles[i]].setBackgroundColor(Color.rgb(113, 74, 67));
                }
                cdtSplash.cancel();
                isClick = true;
                ibtPause.setEnabled(true);
            }
        }.start();
    }

    private void FinishLevel() {
        cdtFinish = new CountDownTimer(1500, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                isClick = false;
            }

            @Override
            public void onFinish() {
                ibtPause.setEnabled(false);
                StartLevel();
            }
        }.start();
    }

    private boolean CheckClickTargetTile(int value) {
        boolean result = false;
        for (int i = 0; i < targetTiles.length; i++) {
            if (targetTiles[i] == value) {
                result = true;
            }
        }
        return result;
    }

    private void Score(int value) {
        if (CheckClickTargetTile(value) && isSelected[value] == false) {
            isSelected[value] = true;
            imageButtons[value].setBackgroundColor(Color.rgb(74, 179, 184));
            numberOfRightTiles++;
            currentScore += scorePerTile;
            if (numberOfRightTiles == numberOfTargetTiles) {
                imageButtons[value].setBackgroundResource(R.drawable.correct);
                currentScore += bonusScore * numberOfTargetTiles;
                numberOfRightAnswer++;
                if (numberOfRightAnswer > 0) {
                    numberOfWrongAnswer = 0;
                }
                if (numberOfRightAnswer == 3 && (numberOfTargetTiles >= 3 && numberOfTargetTiles <= 20)) {
                    numberOfTargetTiles++;
                    numberOfRightAnswer = 0;
                }
                FinishLevel();
            }
        }
        else if (isSelected[value] == true) {
            currentScore += 0;
        }
        else {
            ShowAllTargetTiles();
            imageButtons[value].setBackgroundResource(R.drawable.incorrect);
            numberOfWrongAnswer++;
            if (numberOfWrongAnswer > 0) {
                numberOfRightAnswer = 0;
            }
            if (numberOfWrongAnswer == 3) {
                numberOfTargetTiles--;
                numberOfWrongAnswer = 0;
            }
            FinishLevel();
            if (numberOfTargetTiles < 3) {
                isClick = false;
                txtFinalScore.setText("Score: " + currentScore + "");
                btnStart.setText("PLAY AGAIN");
                ctlInfo.setVisibility(View.GONE);
                ctlBoard.setVisibility(View.GONE);
                txtLogo.setVisibility(View.VISIBLE);
                txtFinalScore.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.VISIBLE);
                currentScore = 0;
                numberOfTargetTiles = 3;
            }
        }

    }

    public void onClick(View v) {
        if (isClick == true) {
            switch (v.getId()) {
                case R.id.ibt1:
                    Score(0);
                    break;
                case R.id.ibt2:
                    Score(1);
                    break;
                case R.id.ibt3:
                    Score(2);
                    break;
                case R.id.ibt4:
                    Score(3);
                    break;
                case R.id.ibt5:
                    Score(4);
                    break;
                case R.id.ibt6:
                    Score(5);
                    break;
                case R.id.ibt7:
                    Score(6);
                    break;
                case R.id.ibt8:
                    Score(7);
                    break;
                case R.id.ibt9:
                    Score(8);
                    break;
                case R.id.ibt10:
                    Score(9);
                    break;
                case R.id.ibt11:
                    Score(10);
                    break;
                case R.id.ibt12:
                    Score(11);
                    break;
                case R.id.ibt13:
                    Score(12);
                    break;
                case R.id.ibt14:
                    Score(13);
                    break;
                case R.id.ibt15:
                    Score(14);
                    break;
                case R.id.ibt16:
                    Score(15);
                    break;
                case R.id.ibt17:
                    Score(16);
                    break;
                case R.id.ibt18:
                    Score(17);
                    break;
                case R.id.ibt19:
                    Score(18);
                    break;
                case R.id.ibt20:
                    Score(19);
                    break;
                case R.id.ibt21:
                    Score(20);
                    break;
                case R.id.ibt22:
                    Score(21);
                    break;
                case R.id.ibt23:
                    Score(22);
                    break;
                case R.id.ibt24:
                    Score(23);
                    break;
                case R.id.ibt25:
                    Score(24);
                    break;
                case R.id.ibt26:
                    Score(25);
                    break;
                case R.id.ibt27:
                    Score(26);
                    break;
                case R.id.ibt28:
                    Score(27);
                    break;
                case R.id.ibt29:
                    Score(28);
                    break;
                case R.id.ibt30:
                    Score(29);
                    break;
                case R.id.ibt31:
                    Score(30);
                    break;
                case R.id.ibt32:
                    Score(31);
                    break;
                case R.id.ibt33:
                    Score(32);
                    break;
                case R.id.ibt34:
                    Score(33);
                    break;
                case R.id.ibt35:
                    Score(34);
                    break;
                case R.id.ibt36:
                    Score(35);
                    break;
            }
            txtScore.setText(currentScore + "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rd = new Random();
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
        txtFinalScore = (TextView) findViewById(R.id.txtFinalScore);
        txtTiles = (TextView) findViewById(R.id.txtTiles);
        btnStart = (Button) findViewById(R.id.btnStart);
        ibtPlay = (ImageButton) findViewById(R.id.ibtPlay);
        ibtPause = (ImageButton) findViewById(R.id.ibtPause);
        ctlInfo = (ConstraintLayout) findViewById(R.id.ctlInfo);
        ctlBoard = (ConstraintLayout) findViewById(R.id.ctlBoard);
        for (int i = 0; i < idArray.length; i++) {
            imageButtons[i] = (ImageButton) findViewById(idArray[i]);
            imageButtons[i].setOnClickListener(this::onClick);
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLogo.setVisibility(View.GONE);
                btnStart.setVisibility(View.GONE);
                txtFinalScore.setVisibility(View.GONE);
                ctlInfo.setVisibility(View.VISIBLE);
                ctlBoard.setVisibility(View.VISIBLE);
                StartLevel();
            }
        });
        ibtPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtPause.setVisibility(v.GONE);
                ibtPlay.setVisibility(v.VISIBLE);
                isClick = false;
            }
        });
        ibtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtPlay.setVisibility(v.GONE);
                ibtPause.setVisibility(v.VISIBLE);
                isClick = true;
            }
        });
    }
}