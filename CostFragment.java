package com.sensoapps.asenousy.thinkmargin;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)

public class CostFragment extends Fragment {

    public EditText VATField, exclVATField, inclVATField, marginField, profitField;
    public TextView costView;
    public Button resetButton;
    public TabLayout priceTabs, marginTabs;
    public Drawable originalDrawable;
    public int sdk = android.os.Build.VERSION.SDK_INT;
    public int jellyBean = android.os.Build.VERSION_CODES.JELLY_BEAN;

    public CostFragment() {
    }

    public static CostFragment newInstance() {
        return new CostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cost_layout, container, false);
        VATField = (EditText) rootView.findViewById(R.id.VAT);
        exclVATField = (EditText) rootView.findViewById(R.id.exclVAT);
        inclVATField = (EditText) rootView.findViewById(R.id.inclVAT);
        marginField = (EditText) rootView.findViewById(R.id.margin);
        profitField = (EditText) rootView.findViewById(R.id.profit);
        costView = (TextView) rootView.findViewById(R.id.cost);
        resetButton = (Button) rootView.findViewById(R.id.reset);
        priceTabs = (TabLayout) rootView.findViewById(R.id.priceTabs);
        marginTabs = (TabLayout) rootView.findViewById(R.id.marginTabs);

        VATField.setOnEditorActionListener(actionListener);
        exclVATField.setOnEditorActionListener(actionListener);
        inclVATField.setOnEditorActionListener(actionListener);
        marginField.setOnEditorActionListener(actionListener);
        profitField.setOnEditorActionListener(actionListener);

        priceTabs.setOnTabSelectedListener(priceTabListener);
        marginTabs.setOnTabSelectedListener(marginTabListener);

        VATField.setOnTouchListener(touchListener);
        exclVATField.setOnTouchListener(touchListener);
        inclVATField.setOnTouchListener(touchListener);
        marginField.setOnTouchListener(touchListener);
        profitField.setOnTouchListener(touchListener);

        originalDrawable = exclVATField.getBackground();

        resetButton.setOnClickListener(clickListener);

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        VATField.clearFocus();
        exclVATField.clearFocus();
        inclVATField.clearFocus();
        marginField.clearFocus();
        profitField.clearFocus();
    }

    EditText.OnEditorActionListener actionListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            v.setCursorVisible(false);
            float VAT, cost, margin, profit, exclVAT, inclVAT;

            VAT = getValue(VATField);

            if (priceTabs.getSelectedTabPosition() == 0) {
                exclVAT = getValue(exclVATField);
                exclVATField.setText(String.format("%.2f", exclVAT));
                inclVAT = exclVAT+(exclVAT*VAT/100);
                inclVATField.setText(String.format("%.2f", inclVAT));
            } else {
                inclVAT = getValue(inclVATField);
                inclVATField.setText(String.format("%.2f", inclVAT));
                exclVAT = inclVAT*100/(100+VAT);
                exclVATField.setText(String.format("%.2f", exclVAT));
            }

            if (marginTabs.getSelectedTabPosition() == 0) {
                margin = getValue(marginField);
                profit = margin*exclVAT/100;
                profitField.setText(String.format("%.2f", profit));
                cost = exclVAT-profit;
            } else {
                profit = getValue(profitField);
                profitField.setText(String.format("%.2f", profit));
                margin = profit*100/exclVAT;
                marginField.setText(String.valueOf(margin));
                cost = exclVAT-profit;
            }

            costView.setText(String.format("%.2f", cost));
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
                    priceTabs.getTabAt(0).select();
                    inclVATField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        exclVATField.setBackgroundDrawable(originalDrawable);
                    } else {
                        exclVATField.setBackground(originalDrawable);
                    }
                } else if (v == inclVATField){
                    priceTabs.getTabAt(1).select();
                    exclVATField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        inclVATField.setBackgroundDrawable(originalDrawable);
                    } else {
                        inclVATField.setBackground(originalDrawable);
                    }
                } else if (v == marginField) {
                    marginTabs.getTabAt(0).select();
                    profitField.setBackgroundColor(Color.TRANSPARENT);
                    if (sdk < jellyBean) {
                        marginField.setBackgroundDrawable(originalDrawable);
                    } else {
                        marginField.setBackground(originalDrawable);
                    }
                } else if (v == profitField){
                    marginTabs.getTabAt(1).select();
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
                exclVATField.setText("");
                inclVATField.setText("");
                marginField.setText("");
                profitField.setText("");
                costView.setText("");
            }
        }
    };

    TabLayout.OnTabSelectedListener priceTabListener = new TabLayout.OnTabSelectedListener() {
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

    TabLayout.OnTabSelectedListener marginTabListener = new TabLayout.OnTabSelectedListener() {
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

