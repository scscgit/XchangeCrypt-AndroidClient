package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.adapters.ExchangeOrderListViewAdapter;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.Order;
import cloud.coders.sk.xchangecrypt.datamodel.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.OrderType;
import cloud.coders.sk.xchangecrypt.listeners.DialogOkClickListener;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by V3502505 on 20/09/2016.
 */
public class ExchangeFragment extends BaseFragment {
    private TextView firstCurrencyText;
    private ImageView firstCurrencyLogo;
    private TextView secondCurrencyText;
    private ImageView secondCurrencyLogo;

    private TextView ballanceText;

    private ListView listViewOrders;

    private EditText amountEdit;
    private TextView amountCoin;

    private EditText priceEdit;
    private TextView priceCoin;

    private EditText feeEdit;
    private TextView feeCoin;

    private EditText sumEdit;
    private TextView sumCoin;

    private Button buttonBuy;
    private Button buttonSell;

    private Button marketOrders;
    private Button userOrders;

    private Button buttonMarketOrder;
    private Button buttonLimitOrder;
    private Button buttonStopOrder;

    private CheckBox stopCheckbox;
    private CheckBox profitCheckbox;

    private EditText stopEditText;
    private EditText profitEditText;

    private LinearLayout firstFrameLinearLayout;
    private LinearLayout secondFrameLinearLayout;
    private LinearLayout thirdFrameLinearLayout;
    private LinearLayout secondFrameAdvancedLinearLayout;

    private LinearLayout coinPairLinearLayout;

    private TextView listViewOrderHeaderBaseCurrency;
    private TextView listViewOrderHeaderQuoteCurrency;

    private ImageView imageUp;
    private ImageView imageDown;
    private ImageView imageDownDown;

    private OrderSide orderSide;

    private RecyclerView recyclerView;

    private AppBarLayout appBarLayout;
    private boolean headerAlreadyCrashed;

    public static ExchangeFragment newInstance(Bundle args) {
        ExchangeFragment fragment = new ExchangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exchange, container, false);
        this.orderSide = OrderSide.buy;
        setActionBar();
        setViews();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        showActionBar();
        setToolbarTitle("Zmenáreň");
        getMainActivity().changeBottomNavigationVisibility(View.VISIBLE);
    }

    @Override
    protected void setViews() {
        firstCurrencyText = rootView.findViewById(R.id.first_coin_logo_text);
        firstCurrencyLogo = rootView.findViewById(R.id.first_coin_logo_image);
        secondCurrencyText = rootView.findViewById(R.id.second_coin_logo_text);
        secondCurrencyLogo = rootView.findViewById(R.id.second_coin_logo_image);
        ballanceText = rootView.findViewById(R.id.exchange_balance_text);
        listViewOrders = rootView.findViewById(R.id.listview_orders);
        amountEdit = rootView.findViewById(R.id.exchange_amount_edit);
        amountCoin = rootView.findViewById(R.id.exchange_amount_coin_text);

        priceEdit = rootView.findViewById(R.id.exchange_price_edit);
        priceCoin = rootView.findViewById(R.id.exchange_price_coin_text);

        feeEdit = rootView.findViewById(R.id.exchange_fee_edit);
        feeCoin = rootView.findViewById(R.id.exchange_fee_coin_text);

        sumEdit = rootView.findViewById(R.id.exchange_sum_edit);
        sumCoin = rootView.findViewById(R.id.exchange_sum_coin_text);

        //buttonOrder = rootView.findViewById(R.id.button_order);

        secondFrameLinearLayout = rootView.findViewById(R.id.linerlayout_order_place);
        thirdFrameLinearLayout = rootView.findViewById(R.id.linerlayout_order_list);
        firstFrameLinearLayout = rootView.findViewById(R.id.linerlayout_order_coin_pair);
        secondFrameAdvancedLinearLayout = rootView.findViewById(R.id.linearLayout_order_advanced);
        coinPairLinearLayout = rootView.findViewById(R.id.linearLayout_coin_pair_select);

        buttonBuy = rootView.findViewById(R.id.exchange_button_exchange_buy);
        buttonSell = rootView.findViewById(R.id.exchange_button_exchange_sell);

        marketOrders = rootView.findViewById(R.id.button_exchange_order_all);
        userOrders = rootView.findViewById(R.id.button_exchange_order_my);

        imageUp = rootView.findViewById(R.id.image_up);
        imageDown = rootView.findViewById(R.id.image_down);
        imageDownDown = rootView.findViewById(R.id.image_down_down);

        buttonMarketOrder = rootView.findViewById(R.id.button_exchange_market);
        buttonLimitOrder = rootView.findViewById(R.id.button_exchange_limit);
        buttonStopOrder = rootView.findViewById(R.id.button_exchange_stop);

        stopCheckbox = rootView.findViewById(R.id.checkbox_stop);
        profitCheckbox = rootView.findViewById(R.id.checkbox_take);

        stopEditText = rootView.findViewById(R.id.edittext_stop);
        profitEditText = rootView.findViewById(R.id.edittext_profit);

//        listViewOrderHeaderBaseCurrency = rootView.findViewById(R.id.listview_orders_header_coin1);
//        listViewOrderHeaderQuoteCurrency = rootView.findViewById(R.id.listview_orders_header_coin2);
    }

    private enum FrameState {
        defaultFrames, secondExpanded, thirdExpanded
    }

    private FrameState frameState;

    private int lastKeyHeight = 0;
    private int lastScreenHeight = 0;

    private ViewGroup lastHeader;

    @Override
    protected void setViewContents() {
        feeEdit.setKeyListener(null);
        sumEdit.setKeyListener(null);
        feeEdit.setText("0,00000001");
        //priceEdit.setText(getContentProvider());

        // TODO: handle null exception
        listViewOrders.setAdapter(new ExchangeOrderListViewAdapter(getContext(), getContentProvider().getMarketOrders().get(getContentProvider().getActualCurrencyPair()), true));
        //listViewOrders.setClickable(false);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.listview_order_header_notype, listViewOrders, false);
        lastHeader = header;
        listViewOrderHeaderBaseCurrency = header.findViewById(R.id.listview_orders_header_coin1);
        listViewOrderHeaderQuoteCurrency = header.findViewById(R.id.listview_orders_header_coin2);
        try {
            listViewOrders.addHeaderView(header);
        } catch (Exception e) {
            headerCrashed();
        }
        updateAfterCurrencyPairChange(getContentProvider().getActualCurrencyPair(), true);
        //setListViewHeightBasedOnChildren(listViewOrders);
        //ballanceText.setText(String.format("%.8f", getContentProvider().getCoinsBalance().get(0).getAmount()) + " " + getContentProvider().getCoinsBalance().get(0).getName());

        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myOrders) {
                    final int position1 = position;
                    DialogOkClickListener dialogOkClickListener = new DialogOkClickListener() {
                        @Override
                        public void onPositiveButtonClicked(Context context) {
                            // Header causes a need to offset all rows by negative one
                            int offset = ExchangeFragment.this.headerAlreadyCrashed ? 0 : -1;
                            ((MainActivity) getActivity()).deleteOrder(currentUserOrders.get(position1 + offset));
                        }
                    };
                    ((MainActivity) getActivity()).showDialogWithAction(R.string.delete, dialogOkClickListener, true);
                }
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (lastKeyHeight != keypadHeight || lastScreenHeight != screenHeight) {
                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        setFrameSizeWhenKeybordShown(true);
                        imageUp.setVisibility(View.GONE);
                        imageDown.setVisibility(View.GONE);
                    } else {
                        if (frameState == FrameState.secondExpanded) {
                            setSecondFrameExpanded(true);
                        } else {
                            setFrameSizeWhenKeybordShown(false);
                        }
                        imageUp.setVisibility(View.VISIBLE);
                        imageDown.setVisibility(View.VISIBLE);
                    }
                }
                lastKeyHeight = keypadHeight;
                lastScreenHeight = screenHeight;
            }
        });
        coinPairLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list1 = getContentProvider().getInstruments();
                final List<String> list2 = new ArrayList<>();
                for (String pair : list1) {
                    list2.add(pair.replace("_", "/"));
                }
                CharSequence[] cs = list2.toArray(new CharSequence[list2.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Vyberte menový pár")
                        .setItems(cs, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateAfterCurrencyPairChange(list2.get(which).replace("/", "_"), false);
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToBuyMode();
            }
        });

        buttonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSellMode();
            }
        });

        marketOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarketOrders();
            }
        });

        userOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserOrders();
            }
        });
        //updateAfterCurrencyPairChange(getContentProvider().getActualCurrencyPair().toString());

        imageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameState == FrameState.secondExpanded) {
                    setDefaultFrames();
                    frameState = FrameState.defaultFrames;
                    imageDown.setVisibility(View.VISIBLE);
                } else {
                    setSecondFrameExpanded(false);
                    frameState = FrameState.thirdExpanded;
                    imageDownDown.setVisibility(View.VISIBLE);
                    imageDown.setVisibility(View.VISIBLE);
                }

            }
        });

        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondFrameExpanded(true);
                frameState = FrameState.secondExpanded;
                imageDown.setVisibility(View.GONE);
            }
        });

        imageDownDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFrames();
                frameState = FrameState.defaultFrames;
                imageDownDown.setVisibility(View.GONE);
            }
        });

        priceEdit.addTextChangedListener(new TextWatcher() {
            private boolean alreadyChanged = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!this.alreadyChanged) {
                    this.alreadyChanged = true;
                    // Unlocks the price for edit until a user restarts the Fragment
                    priceEdit.setTextColor(feeEdit.getCurrentTextColor());
                    stopCheckbox.setEnabled(true);
                    profitCheckbox.setEnabled(true);
                    buttonLimitOrder.setEnabled(true);
                    buttonLimitOrder.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.item_background_orange));
                    buttonStopOrder.setEnabled(true);
                    buttonStopOrder.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.item_background_orange));
                    // Initializes the stop and profit targets @ current price for easier edit
                    profitEditText.setText(priceEdit.getText().toString());
                    stopEditText.setText(priceEdit.getText().toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
                    double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
                    double fee = Double.parseDouble(feeEdit.getText().toString().replace(",", "."));
                    double sum = amount * price + fee;
                    sumEdit.setText(String.format("%.8f", sum));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
                    double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
                    double fee = Double.parseDouble(feeEdit.getText().toString().replace(",", "."));
                    double sum = amount * price + fee;
                    sumEdit.setText(String.format("%.8f", sum));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        stopCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stopEditText.setEnabled(true);
                } else {
                    stopEditText.setEnabled(false);
                }
            }
        });

        profitCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    profitEditText.setEnabled(true);
                } else {
                    profitEditText.setEnabled(false);
                }
            }
        });

        buttonMarketOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    confirmationDialog(
                            getContext(),
                            "Market ponuka",
                            "Potvrďte prosím " + (orderSide == OrderSide.buy ? "nákup" : "predaj") + " za aktuálnu ponuku trhu",
                            new Runnable() {
                                @Override
                                public void run() {
                                    sendMarketOrder();
                                }
                            }
                    );
                } else {
                    Toast.makeText(getContext(), "Musíte zadať množstvo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLimitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    confirmationDialog(
                            getContext(),
                            "Limit ponuka",
                            "Potvrďte prosím vytvorenie limit ponuky na " + (orderSide == OrderSide.buy ? "nákup" : "predaj"),
                            new Runnable() {
                                @Override
                                public void run() {
                                    sendLimitOrder();
                                }
                            }
                    );
                } else {
                    Toast.makeText(getContext(), "Musíte zadať množstvo a cenu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonStopOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountEdit.getText().toString().trim().length() > 0) {
                    confirmationDialog(
                            getContext(),
                            "Stop ponuka",
                            "Potvrďte prosím vytvorenie stop ponuky na " + (orderSide == OrderSide.buy ? "nákup" : "predaj"),
                            new Runnable() {
                                @Override
                                public void run() {
                                    sendStopOrder();
                                }
                            }
                    );
                } else {
                    Toast.makeText(getContext(), "Musíte zadať množstvo a cenu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMarketOrder() {
        String pairs = getContentProvider().getActualCurrencyPair();
        String[] pair = pairs.split("_");
        double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
        double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
        Order order = new Order(null, null,
                pair[0],
                amount,
                pair[1],
                price * amount,
                orderSide,
                OrderType.market
        );
        ((MainActivity) getActivity()).sendOrder(order);
    }

    private void sendLimitOrder() {
        String pairs = getContentProvider().getActualCurrencyPair();
        String[] pair = pairs.split("_");
        double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
        double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));

        Double stopLoss = null;
        if (stopCheckbox.isChecked()) {
            if (stopEditText.getText().toString().trim().length() > 0) {
                stopLoss = Double.parseDouble(stopEditText.getText().toString().replace(",", "."));
            } else {
                Toast.makeText(getContext(), "Zadajte hodnotu pre stop loss.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Double takeProfit = null;
        if (profitCheckbox.isChecked()) {
            if (profitEditText.getText().toString().trim().length() > 0) {
                takeProfit = Double.parseDouble(profitEditText.getText().toString().replace(",", "."));
            } else {
                Toast.makeText(getContext(), "Zadajte hodnotu pre take profit.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Order order = new Order(
                price,
                null,
                stopLoss,
                takeProfit,
                pair[0],
                Double.parseDouble(amountEdit.getText().toString().replace(",", ".")),
                pair[1],
                price * amount,
                orderSide,
                OrderType.limit
        );

        ((MainActivity) getActivity()).sendOrder(order);
    }

    private void sendStopOrder() {
        String pairs = getContentProvider().getActualCurrencyPair();
        String[] pair = pairs.split("_");
        double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
        double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));

        Double stopLoss = null;
        if (stopCheckbox.isChecked()) {
            if (stopEditText.getText().toString().trim().length() > 0) {
                stopLoss = Double.parseDouble(stopEditText.getText().toString().replace(",", "."));
            } else {
                Toast.makeText(getContext(), "Zadajte hodnotu pre stop loss.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Double takeProfit = null;
        if (profitCheckbox.isChecked()) {
            if (profitEditText.getText().toString().trim().length() > 0) {
                takeProfit = Double.parseDouble(profitEditText.getText().toString().replace(",", "."));
            } else {
                Toast.makeText(getContext(), "Zadajte hodnotu pre take profit.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Order order = new Order(
                null,
                price,
                stopLoss,
                takeProfit,
                pair[0],
                amount,
                pair[1],
                price * amount,
                orderSide, OrderType.stop
        );
        ((MainActivity) getActivity()).sendOrder(order);
    }

    private boolean myOrders = false;

    public void switchToBuyMode() {
        getContentProvider().setCurrentOrderSide(OrderSide.buy);
        buttonBuy.setBackgroundColor(getResources().getColor(R.color.orange));
        buttonSell.setBackgroundColor(getResources().getColor(R.color.gray));
        String[] pairParts = getContentProvider().getActualCurrencyPair().split("_");
        Coin quoteCoin = getContentProvider().getCoinByName(pairParts[1]);
        ballanceText.setText(String.format("%.8f", quoteCoin.getAmount()) + " " + quoteCoin.getName());
        if (myOrders) {
            showUserOrders();
        } else {
            showMarketOrders();
        }
        priceEdit.setText(String.format("%.8f", getContentProvider().getMarketPricePerPair(getContentProvider().getActualCurrencyPair(), getContentProvider().getCurrentOrderSide())));
    }

    public void switchToSellMode() {
        getContentProvider().setCurrentOrderSide(OrderSide.sell);
        buttonBuy.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonSell.setBackgroundColor(getResources().getColor(R.color.orange));
        String[] pairParts = getContentProvider().getActualCurrencyPair().split("_");
        Coin baseCoin = getContentProvider().getCoinByName(pairParts[0]);
        ballanceText.setText(String.format("%.8f", baseCoin.getAmount()) + " " + baseCoin.getName());
        if (myOrders) {
            showUserOrders();
        } else {
            showMarketOrders();
        }
        priceEdit.setText(String.format("%.8f", getContentProvider().getMarketPricePerPair(getContentProvider().getActualCurrencyPair(), getContentProvider().getCurrentOrderSide())));
    }

    private List<Order> currentUserOrders = null;

    private void showMarketOrders() {
        String[] pair = getContentProvider().getActualCurrencyPair().split("_");
        List<Order> marketOrderForPairAndSide = getContentProvider().getMarketOrderForPairAndSide(pair[0], pair[1], getContentProvider().getCurrentOrderSide());
        listViewOrders.setAdapter(new ExchangeOrderListViewAdapter(getContext(), marketOrderForPairAndSide, true));
        marketOrders.setBackgroundColor(getResources().getColor(R.color.orange));
        userOrders.setBackgroundColor(getResources().getColor(R.color.gray));
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.listview_order_header_notype, listViewOrders, false);
        listViewOrderHeaderBaseCurrency = header.findViewById(R.id.listview_orders_header_coin1);
        listViewOrderHeaderQuoteCurrency = header.findViewById(R.id.listview_orders_header_coin2);
        listViewOrders.removeHeaderView(lastHeader);
        try {
            listViewOrders.addHeaderView(header);
        } catch (IllegalStateException e) {
            headerCrashed();
        }
        lastHeader = header;
        myOrders = false;
    }

    private void showUserOrders() {
        String[] pair = getContentProvider().getActualCurrencyPair().split("_");
        currentUserOrders = getContentProvider().getUserOrdersByCurrencyPairAndSide(pair[0], pair[1], orderSide);
        listViewOrders.setAdapter(new ExchangeOrderListViewAdapter(getContext(), currentUserOrders, false));
        marketOrders.setBackgroundColor(getResources().getColor(R.color.gray));
        userOrders.setBackgroundColor(getResources().getColor(R.color.orange));
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.listview_order_header, listViewOrders, false);
        listViewOrderHeaderBaseCurrency = header.findViewById(R.id.listview_orders_header_coin1);
        listViewOrderHeaderQuoteCurrency = header.findViewById(R.id.listview_orders_header_coin2);
        listViewOrders.removeHeaderView(lastHeader);
        try {
            listViewOrders.addHeaderView(header);
        } catch (IllegalStateException e) {
            headerCrashed();
        }
        lastHeader = header;
        myOrders = true;
    }

    private void setLogo(String coin, ImageView logo) {
        switch (coin) {
            case "BTC":
                logo.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.btc_icon));
                break;
            case "QBC":
                logo.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.qbc_icon));
        }
    }

    private void updateAfterCurrencyPairChange(String pair, boolean hasData) {
        if (!hasData) {
            getContentProvider().setActualCurrencyPair(pair);
            ((MainActivity) getActivity()).getDataBeforeSwitch(FRAGMENT_EXCHANGE, null, true);
            return;
        }
        //getMainActivity().getTradingApiHelper().tradingOffersForCurrencyPair(MainActivity.asyncTaskId++,pair);
        //getMainActivity().showProgressDialog("Načítavám dáta");
        priceEdit.setText(String.format("%.8f", getContentProvider().getMarketPricePerPair(pair, orderSide)));
        String[] pairParts = pair.split("_");
        firstCurrencyText.setText(pairParts[0]);
        secondCurrencyText.setText(pairParts[1]);
        setLogo(pairParts[0], firstCurrencyLogo);
        setLogo(pairParts[1], secondCurrencyLogo);
        getContentProvider().setActualCurrencyPair(pair);

        amountCoin.setText(pairParts[0]);
        priceCoin.setText(pairParts[1]);
        feeCoin.setText(pairParts[1]);
        sumCoin.setText(pairParts[1]);

        listViewOrderHeaderBaseCurrency.setText(pairParts[0]);
        listViewOrderHeaderBaseCurrency.setText(pairParts[1]);
        myOrders = false;
        switchToBuyMode();
    }

    public void setSecondFrameExpanded(boolean isExpanded) {
        LinearLayout.LayoutParams param1;
        LinearLayout.LayoutParams param2;
        LinearLayout.LayoutParams param3;
        LinearLayout.LayoutParams param4;
        if (isExpanded) {
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    4.0f
            );
            param3 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0.0f
            );
            param4 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    2f
            );
        } else {
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0f
            );
            param3 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    6f
            );
            param4 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0f
            );
        }
        firstFrameLinearLayout.setLayoutParams(param1);
        secondFrameLinearLayout.setLayoutParams(param2);
        thirdFrameLinearLayout.setLayoutParams(param3);
        secondFrameAdvancedLinearLayout.setLayoutParams(param4);
    }

    public void setDefaultFrames() {
        LinearLayout.LayoutParams param1;
        LinearLayout.LayoutParams param2;
        LinearLayout.LayoutParams param3;
        LinearLayout.LayoutParams param4;
        param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                3.0f
        );
        param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                3.0f
        );
        param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
        );
        param4 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                0f
        );
        firstFrameLinearLayout.setLayoutParams(param3);
        secondFrameLinearLayout.setLayoutParams(param2);
        thirdFrameLinearLayout.setLayoutParams(param1);
        secondFrameAdvancedLinearLayout.setLayoutParams(param4);
    }

    public void setFrameSizeWhenKeybordShown(boolean isExpanded) {
        LinearLayout.LayoutParams param1;
        LinearLayout.LayoutParams param2;
        LinearLayout.LayoutParams param3;
        LinearLayout.LayoutParams param4;
        if (isExpanded) {
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0f
            );
            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    5.0f
            );
            param3 = param1;
            param4 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    2f
            );
        } else {
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    3.0f
            );
            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    3.0f
            );
            param3 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            param4 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0f
            );
        }
        thirdFrameLinearLayout.setLayoutParams(param1);
        secondFrameLinearLayout.setLayoutParams(param2);
        firstFrameLinearLayout.setLayoutParams(param3);
        secondFrameAdvancedLinearLayout.setLayoutParams(param4);
    }

    private void headerCrashed() {
        if (!this.headerAlreadyCrashed) {
            this.headerAlreadyCrashed = true;
            Toast.makeText(getContext(), "Cannot add header, low Android API level", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmationDialog(final Context context, String title, String message, final Runnable action) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (whichButton == BUTTON_POSITIVE) {
                            action.run();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
