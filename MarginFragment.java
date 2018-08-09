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

public class MarginFragment extends Fragment {

    public EditText VATField, costField, exclVATField, inclVATField;
    public TextView marginView, profitView;
    public Button resetButton;
    public TabLayout tabs;
    public Drawable originalDrawable;
    public int sdk = android.os.Build.VERSION.SDK_INT;
    public int jellyBean = android.os.Build.VERSION_CODES.JELLY_BEAN;

    public MarginFragment() {
    }

    public static MarginFragment newInstance() {
        return new MarginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_margin_layout, container, false);
        VATField = (EditText) rootView.findViewById(R.id.VAT);
        costField = (EditText) rootView.findViewById(R.id.cost);
        exclVATField = (EditText) rootView.findViewById(R.id.exclVAT);
        inclVATField = (EditText) rootView.findViewById(R.id.inclVAT);
        marginView = (TextView) rootView.findViewById(R.id.margin);
        profitView = (TextView) rootView.findViewById(R.id.profit);
        resetButton = (Button) rootView.findViewById(R.id.reset);

        tabs = (TabLayout) rootView.findViewById(R.id.tabs);

        VATField.setOnEditorActionListener(actionListener);
        costField.setOnEditorActionListener(actionListener);
        exclVATField.setOnEditorActionListener(actionListener);
        inclVATField.setOnEditorActionListener(actionListener);
        tabs.setOnTabSelectedListener(tabListener);

        VATField.setOnTouchListener(touchListener);
        costField.setOnTouchListener(touchListener);
        exclVATField.setOnTouchListener(touchListener);
        inclVATField.setOnTouchListener(touchListener);

        originalDrawable = exclVATField.getBackground();

        resetButton.setOnClickListener(clickListener);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        VATField.clearFocus();
        costField.clearFocus();
        exclVATField.clearFocus();
        inclVATField.clearFocus();
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
                exclVAT = getValue(exclVATField);
                exclVATField.setText(String.format("%.2f", exclVAT));
                inclVAT = exclVAT+(exclVAT*VAT/100);
                profit = exclVAT - cost;
                margin = (1 - cost/exclVAT)*100;
                inclVATField.setText(String.format("%.2f", inclVAT));
                marginView.setText(String.format("%.2f", margin));
                profitView.setText(String.format("%.2f", profit));
            } else {
                inclVAT = getValue(inclVATField);
                inclVATField.setText(String.format("%.2f", inclVAT));
                exclVAT = inclVAT*100/(100+VAT);
                profit = exclVAT - cost;
                margin = (1 - cost/exclVAT)*100;
                exclVATField.setText(String.format("%.2f", exclVAT));
                marginView.setText(String.format("%.2f", margin));
                profitView.setText(String.format("%.2f", profit));
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

                if (v == exclVATField) {
                    tabs.getTabAt(0).select();
                    inclVATField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        exclVATField.setBackgroundDrawable(originalDrawable);
                    } else {
                        exclVATField.setBackground(originalDrawable);
                    }
                } else if (v == inclVATField){
                    tabs.getTabAt(1).select();
                    exclVATField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        inclVATField.setBackgroundDrawable(originalDrawable);
                    } else {
                        inclVATField.setBackground(originalDrawable);
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
                exclVATField.setText("");
                inclVATField.setText("");
                marginView.setText("");
                profitView.setText("");
            }
        }
    };

    TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getPosition() == 0) {
                inclVATField.setBackgroundColor(Color.TRANSPARENT);
                if (sdk < jellyBean) {
                    exclVATField.setBackgroundDrawable(originalDrawable);
                } else {
                    exclVATField.setBackground(originalDrawable);
                }
                exclVATField.requestFocus();
            } else {
                exclVATField.setBackgroundColor(Color.TRANSPARENT);

                if (sdk < jellyBean) {
                    inclVATField.setBackgroundDrawable(originalDrawable);
                } else {
                    inclVATField.setBackground(originalDrawable);
                }
                inclVATField.requestFocus();
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

