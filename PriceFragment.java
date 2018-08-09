package com.sensoapps.asenousy.thinkmargin;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)

public class PriceFragment extends Fragment {

    public EditText VATField, costField, marginField, profitField;
    public TextView exclVATView, inclVATView;
    public Button resetButton;
    public TabLayout tabs;
    public Drawable originalDrawable;
    public int sdk = android.os.Build.VERSION.SDK_INT;
    public int jellyBean = android.os.Build.VERSION_CODES.JELLY_BEAN;

    public PriceFragment() {
    }

    public static PriceFragment newInstance() {
        return new PriceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_price_layout, container, false);
        VATField = (EditText) rootView.findViewById(R.id.VAT);
        costField = (EditText) rootView.findViewById(R.id.cost);
        marginField = (EditText) rootView.findViewById(R.id.margin);
        profitField = (EditText) rootView.findViewById(R.id.profit);
        exclVATView = (TextView) rootView.findViewById(R.id.exclVAT);
        inclVATView = (TextView) rootView.findViewById(R.id.inclVAT);
        resetButton = (Button) rootView.findViewById(R.id.reset);
        tabs = (TabLayout) rootView.findViewById(R.id.tabs);

        VATField.setOnEditorActionListener(actionListener);
        costField.setOnEditorActionListener(actionListener);
        marginField.setOnEditorActionListener(actionListener);
        profitField.setOnEditorActionListener(actionListener);

        tabs.setOnTabSelectedListener(tabListener);

        VATField.setOnTouchListener(touchListener);
        costField.setOnTouchListener(touchListener);
        marginField.setOnTouchListener(touchListener);
        profitField.setOnTouchListener(touchListener);

        originalDrawable = profitField.getBackground();

        resetButton.setOnClickListener(clickListener);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        VATField.clearFocus();
        costField.clearFocus();
        marginField.clearFocus();
        profitField.clearFocus();
    }

    EditText.OnEditorActionListener actionListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            v.setCursorVisible(false);
            float VAT, cost, margin, profit, exclVAT, inclVAT;

            VAT = getValue(VATField);
            cost = getValue(costField);
            costField.setText(String.format("%.2f", cost));

            if (tabs.getSelectedTabPosition() == 0) {
                margin = getValue(marginField);
                marginField.setText(String.format("%.2f", margin));
                exclVAT = cost/(1-(margin/100));
                profit = exclVAT - cost;
                inclVAT = exclVAT+(exclVAT*VAT/100);
                profitField.setText(String.format("%.2f", profit));
                exclVATView.setText(String.format("%.2f", exclVAT));
                inclVATView.setText(String.format("%.2f", inclVAT));
            } else {
                profit = getValue(profitField);
                profitField.setText(String.format("%.2f", profit));
                margin = profit*100/(cost+profit);
                exclVAT = cost/(1-(margin/100));
                inclVAT = exclVAT+(exclVAT*VAT/100);
                marginField.setText(String.format("%.2f", margin));
                exclVATView.setText(String.format("%.2f", exclVAT));
                inclVATView.setText(String.format("%.2f", inclVAT));
            }
            return false;
        }
    };

    EditText.OnTouchListener touchListener = new EditText.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

                EditText f = (EditText)v;
                f.setText("");

                 ((EditText) v).setCursorVisible(true);

                if (v == marginField) {
                    tabs.getTabAt(0).select();
                    profitField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        marginField.setBackgroundDrawable(originalDrawable);
                    } else {
                        marginField.setBackground(originalDrawable);
                    }
                } else if (v == profitField){
                    tabs.getTabAt(1).select();
                    marginField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        profitField.setBackgroundDrawable(originalDrawable);
                    } else {
                        profitField.setBackground(originalDrawable);
                    }
                }
            }
            return false;
        }
    };

    Button.OnClickListener clickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == resetButton) {
                VATField.setText("");
                costField.setText("");
                marginField.setText("");
                profitField.setText("");
                exclVATView.setText("");
                inclVATView.setText("");
            }
        }
    };

    TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getPosition() == 0) {
                profitField.setBackgroundColor(Color.TRANSPARENT);
                if (sdk < jellyBean) {
                    marginField.setBackgroundDrawable(originalDrawable);
                } else {
                    marginField.setBackground(originalDrawable);
                }
                marginField.requestFocus();
            } else {
                marginField.setBackgroundColor(Color.TRANSPARENT);
                if (sdk < jellyBean) {
                    profitField.setBackgroundDrawable(originalDrawable);
                } else {
                    profitField.setBackground(originalDrawable);
                }
                profitField.requestFocus();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    public float getValue(EditText field) {
        try {
            return new Float(field.getText().toString());
        } catch (Exception e) {
            field.setText("0");
            return 0;
        }
    }
}

