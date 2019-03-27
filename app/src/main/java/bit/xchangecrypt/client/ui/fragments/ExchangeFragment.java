package bit.xchangecrypt.client.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import androidx.core.content.ContextCompat;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.adapters.ExchangeOrderListViewAdapter;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.datamodel.enums.OrderType;
import bit.xchangecrypt.client.listeners.DialogOkClickListener;
import bit.xchangecrypt.client.util.CoinHelper;
import bit.xchangecrypt.client.util.DateFormatter;
import bit.xchangecrypt.client.util.DialogHelper;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import io.swagger.client.model.BarsArrays;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by V3502505 on 20/09/2016.
 */
public class ExchangeFragment extends BaseFragment {
    private TextView firstCurrencyText;
    private ImageView firstCurrencyLogo;
    private TextView secondCurrencyText;
    private ImageView secondCurrencyLogo;

    private TextView balanceText;

    private ListView ordersList;
    private CandleStickChart chart;

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

    private Button marketOrdersButton;
    private Button userOrdersButton;
    private Button graphButton;

    private LinearLayout orderListDescriptionText;
    private LinearLayout orderListDescriptionResolution;
    private Button resolutionMonth;
    private Button resolutionDay;
    private Button resolutionHour;

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

    private ImageView imageUp;
    private ImageView imageDown;
    private ImageView imageDownDown;

    private OrderSide orderSide;

    private boolean headerAlreadyCrashed;

    private ExchangeFragmentState fragmentState;

    private int lastKeyHeight = 0;
    private int lastScreenHeight = 0;

    private ViewGroup header;
    private boolean myOrders = false;
    private boolean displayGraph = false;

    private enum ExchangeFragmentState {
        DEFAULT, ADVANCED_ORDER_PLACEMENT, EXPANDED_ORDERS_LIST
    }

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
        setActionBar();
        setViews();
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
        balanceText = rootView.findViewById(R.id.exchange_coin_balance_value);
        ordersList = rootView.findViewById(R.id.exchange_orders_list);

        chart = rootView.findViewById(R.id.exchange_chart);
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        amountEdit = rootView.findViewById(R.id.exchange_amount_edit);
        amountCoin = rootView.findViewById(R.id.exchange_amount_coin_text);

        priceEdit = rootView.findViewById(R.id.exchange_price_edit);
        priceCoin = rootView.findViewById(R.id.exchange_price_coin_text);

        feeEdit = rootView.findViewById(R.id.exchange_fee_edit);
        feeCoin = rootView.findViewById(R.id.exchange_fee_coin_text);

        sumEdit = rootView.findViewById(R.id.exchange_sum_edit);
        sumCoin = rootView.findViewById(R.id.exchange_sum_coin_text);

        //buttonOrder = rootView.findViewById(R.id.button_order);

        secondFrameLinearLayout = rootView.findViewById(R.id.exchange_order_placement);
        thirdFrameLinearLayout = rootView.findViewById(R.id.exchange_order_list);
        firstFrameLinearLayout = rootView.findViewById(R.id.linerlayout_order_coin_pair);
        secondFrameAdvancedLinearLayout = rootView.findViewById(R.id.exchange_order_advanced);
        coinPairLinearLayout = rootView.findViewById(R.id.linearLayout_coin_pair_select);

        buttonBuy = rootView.findViewById(R.id.exchange_button_switch_buy);
        buttonSell = rootView.findViewById(R.id.exchange_button_switch_sell);

        marketOrdersButton = rootView.findViewById(R.id.exchange_orders_button_market);
        userOrdersButton = rootView.findViewById(R.id.exchange_orders_button_my);
        graphButton = rootView.findViewById(R.id.exchange_orders_button_graph);

        orderListDescriptionText = rootView.findViewById(R.id.exchange_order_list_description_text);
        orderListDescriptionResolution = rootView.findViewById(R.id.exchange_order_list_description_resolution);
        resolutionMonth = rootView.findViewById(R.id.exchange_resolution_1M);
        resolutionDay = rootView.findViewById(R.id.exchange_resolution_1D);
        resolutionHour = rootView.findViewById(R.id.exchange_resolution_1H);

        imageUp = rootView.findViewById(R.id.exchange_state_arrow_up);
        imageDown = rootView.findViewById(R.id.exchange_state_arrow_down);
        imageDownDown = rootView.findViewById(R.id.exchange_state_arrow_down_hidden);

        buttonMarketOrder = rootView.findViewById(R.id.exchange_button_market);
        buttonLimitOrder = rootView.findViewById(R.id.exchange_button_limit);
        buttonStopOrder = rootView.findViewById(R.id.exchange_button_stop);

        stopCheckbox = rootView.findViewById(R.id.exchange_stoploss_checkbox);
        profitCheckbox = rootView.findViewById(R.id.exchange_takeprofit_checkbox);

        stopEditText = rootView.findViewById(R.id.exchange_stoploss_edit);
        profitEditText = rootView.findViewById(R.id.exchange_takeprofit_edit);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.orderSide = getContentProvider().getCurrentOrderSide();
        // By default initializes order side to buy
        if (this.orderSide == null) {
            getContentProvider().setCurrentOrderSide(OrderSide.BUY);
            this.orderSide = OrderSide.BUY;
        }
        setViewContents();
    }

    @Override
    protected void setViewContents() {
        // Disable editing of generated text
        feeEdit.setKeyListener(null);
        sumEdit.setKeyListener(null);
        feeEdit.setText("0,00000001");

        // Initialize generated values and displayed orders
        updateOnCurrencyPairChange(getContentProvider().getCurrentCurrencyPair(), true);

        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 0) {
                    // Clicked on a header, ignored
                    return;
                }
                if (myOrders) {
                    DialogOkClickListener dialogOkClickListener = new DialogOkClickListener() {
                        @Override
                        public void onPositiveButtonClicked(Context context) {
                            // Crashed header causes a need to offset all rows by negative one
                            int offset = ExchangeFragment.this.headerAlreadyCrashed ? 0 : -1;
                            getMainActivity().deleteOrder(currentUserOrders.get(position + offset));
                        }
                    };
                    getMainActivity().showDialogWithAction(R.string.order_delete, dialogOkClickListener, true);
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
                        setFrameSizeWhenKeyboardShown(true);
                        imageUp.setVisibility(View.GONE);
                        imageDown.setVisibility(View.GONE);
                    } else {
                        if (fragmentState == ExchangeFragmentState.ADVANCED_ORDER_PLACEMENT) {
                            setSecondFrameExpanded(true);
                        } else {
                            setFrameSizeWhenKeyboardShown(false);
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
                            updateOnCurrencyPairChange(list2.get(which).replace("/", "_"), false);
                        }
                    });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToBuyMode(true);
            }
        });

        buttonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSellMode(true);
            }
        });

        marketOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMarketDepthOrders();
            }
        });

        userOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContentProvider().getUser() == null) {
                    getMainActivity().loginDialog();
                    return;
                }
                showUserOrders();
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGraph();
            }
        });

        resolutionDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolutionDay.setBackgroundColor(getResources().getColor(R.color.blue));
                getContentProvider().setGraphResolution("1D");
                getMainActivity().getContentRefresher().pauseRefresher().startRefresher();
                // Wait for refresher
            }
        });

        resolutionHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolutionHour.setBackgroundColor(getResources().getColor(R.color.blue));
                getContentProvider().setGraphResolution("1H");
                getMainActivity().getContentRefresher().pauseRefresher().startRefresher();
                // Wait for refresher
            }
        });

        resolutionMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolutionMonth.setBackgroundColor(getResources().getColor(R.color.blue));
                getContentProvider().setGraphResolution("1M");
                getMainActivity().getContentRefresher().pauseRefresher().startRefresher();
                // Wait for refresher
            }
        });

        imageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentState == ExchangeFragmentState.ADVANCED_ORDER_PLACEMENT) {
                    setDefaultFrames();
                    fragmentState = ExchangeFragmentState.DEFAULT;
                    imageDown.setVisibility(View.VISIBLE);
                } else {
                    setSecondFrameExpanded(false);
                    fragmentState = ExchangeFragmentState.EXPANDED_ORDERS_LIST;
                    imageDownDown.setVisibility(View.VISIBLE);
                    imageDown.setVisibility(View.VISIBLE);
                }

            }
        });

        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSecondFrameExpanded(true);
                fragmentState = ExchangeFragmentState.ADVANCED_ORDER_PLACEMENT;
                imageDown.setVisibility(View.GONE);
            }
        });

        imageDownDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultFrames();
                fragmentState = ExchangeFragmentState.DEFAULT;
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
                    try {
                        double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
                        double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
                        double fee = Double.parseDouble(feeEdit.getText().toString().replace(",", "."));
                        double sum = amount * price + fee;
                        sumEdit.setText(String.format("%.8f", sum));
                    } catch (NumberFormatException e) {
                        sumEdit.setText("Chybná hodnota");
                    }
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
                    try {
                        double amount = Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
                        double price = Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
                        double fee = Double.parseDouble(feeEdit.getText().toString().replace(",", "."));
                        double sum = amount * price + fee;
                        sumEdit.setText(String.format("%.8f", sum));
                    } catch (NumberFormatException e) {
                        sumEdit.setText("Chybná hodnota");
                    }
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
                if (getContentProvider().getUser() == null) {
                    getMainActivity().loginDialog();
                    return;
                }
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    DialogHelper.confirmationDialog(
                        getContext(),
                        "Market ponuka",
                        "Potvrďte prosím " + (orderSide == OrderSide.BUY ? "nákup" : "predaj") + " za aktuálnu ponuku trhu",
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
                if (getContentProvider().getUser() == null) {
                    getMainActivity().loginDialog();
                    return;
                }
                if (amountEdit.getText().toString().trim().length() > 0 && priceEdit.getText().toString().trim().length() > 0) {
                    DialogHelper.confirmationDialog(
                        getContext(),
                        "Limit ponuka",
                        "Potvrďte prosím vytvorenie limit ponuky na " + (orderSide == OrderSide.BUY ? "nákup" : "predaj"),
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
                if (getContentProvider().getUser() == null) {
                    getMainActivity().loginDialog();
                    return;
                }
                if (amountEdit.getText().toString().trim().length() > 0) {
                    DialogHelper.confirmationDialog(
                        getContext(),
                        "Stop ponuka",
                        "Potvrďte prosím vytvorenie stop ponuky na " + (orderSide == OrderSide.BUY ? "nákup" : "predaj"),
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

    private void createOrdersHeader(boolean userOrders) {
        ordersList.removeHeaderView(header);
        header = (ViewGroup) getLayoutInflater().inflate(
            userOrders
                ? R.layout.item_order_user_header
                : R.layout.item_order_depth_header,
            ordersList,
            false
        );
        String[] currencies = getContentProvider().getCurrentCurrencyPair().split("_");
        TextView ordersHeaderBaseCurrency = header.findViewById(R.id.listview_orders_header_coin1);
        ordersHeaderBaseCurrency.setText(currencies[0]);
        TextView ordersHeaderQuoteCurrency = header.findViewById(R.id.listview_orders_header_coin2);
        ordersHeaderQuoteCurrency.setText(currencies[1]);
        try {
            ordersList.addHeaderView(header);
        } catch (Exception e) {
            if (!this.headerAlreadyCrashed) {
                this.headerAlreadyCrashed = true;
                Toast.makeText(getContext(), "Cannot add header, low Android API level", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMarketOrder() {
        String pairs = getContentProvider().getCurrentCurrencyPair();
        String[] pair = pairs.split("_");
        Double amount = parseAmountEdit();
        if (amount == null) {
            return;
        }
        Order order = new Order(null, null,
            pair[0],
            amount,
            pair[1],
            orderSide,
            OrderType.MARKET
        );
        getMainActivity().sendOrder(order);
    }

    private void sendLimitOrder() {
        String pairs = getContentProvider().getCurrentCurrencyPair();
        String[] pair = pairs.split("_");
        Double price = parsePriceEdit();
        if (price == null) {
            return;
        }

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
            orderSide,
            OrderType.LIMIT
        );
        getMainActivity().sendOrder(order);
    }

    private void sendStopOrder() {
        String pairs = getContentProvider().getCurrentCurrencyPair();
        String[] pair = pairs.split("_");
        Double price = parsePriceEdit();
        Double amount = parseAmountEdit();
        if (price == null || amount == null) {
            return;
        }

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
            orderSide,
            OrderType.STOP
        );
        getMainActivity().sendOrder(order);
    }


    private Double parseAmountEdit() {
        try {
            return Double.parseDouble(amountEdit.getText().toString().replace(",", "."));
        } catch (NumberFormatException e) {
            DialogHelper.alertDialog(getMainActivity(), "Chybná hodnota", "Prosím opravte zadanú hodnotu množstva");
            return null;
        }
    }

    private Double parsePriceEdit() {
        try {
            return Double.parseDouble(priceEdit.getText().toString().replace(",", "."));
        } catch (NumberFormatException e) {
            DialogHelper.alertDialog(getMainActivity(), "Chybná hodnota", "Prosím opravte zadanú hodnotu ceny");
            return null;
        }
    }

    public void switchToBuyMode(boolean forceResetPrice) {
        getContentProvider().setCurrentOrderSide(OrderSide.BUY);
        orderSide = OrderSide.BUY;
        buttonBuy.setBackgroundColor(getResources().getColor(R.color.orange));
        buttonSell.setBackgroundColor(getResources().getColor(R.color.gray));
        showCoinBalance();
        if (displayGraph) {
            showGraph();
        } else if (myOrders) {
            showUserOrders();
        } else {
            showMarketDepthOrders();
        }

        // Only edit the price as long as it's an initialization
        if (forceResetPrice || priceEdit.getText().toString().equals("")) {
            priceEdit.setText(String.format("%.8f", getContentProvider().getMarketPrice(getContentProvider().getCurrentCurrencyPair(), orderSide)));
        }
    }

    public void switchToSellMode(boolean forceResetPrice) {
        getContentProvider().setCurrentOrderSide(OrderSide.SELL);
        orderSide = OrderSide.SELL;
        buttonBuy.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonSell.setBackgroundColor(getResources().getColor(R.color.orange));
        showCoinBalance();
        if (displayGraph) {
            showGraph();
        } else if (myOrders) {
            showUserOrders();
        } else {
            showMarketDepthOrders();
        }

        // Only edit the price as long as it's an initialization
        if (forceResetPrice || priceEdit.getText().toString().equals("")) {
            priceEdit.setText(String.format("%.8f", getContentProvider().getMarketPrice(getContentProvider().getCurrentCurrencyPair(), orderSide)));
        }
    }

    private void showCoinBalance() {
        String[] currencies = getContentProvider().getCurrentCurrencyPair().split("_");
        Coin coin = getContentProvider().getCoinBalanceByName(currencies[orderSide == OrderSide.SELL ? 0 : 1]);
        if (coin == null) {
            balanceText.setText("");
        } else {
            balanceText.setText(String.format("%.8f", coin.getAmount()).replaceAll("0+$", "0") + " " + coin.getSymbolName());
        }
    }

    private List<Order> currentUserOrders = null;

    private void showMarketDepthOrders() {
        this.ordersList.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        this.chart.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        marketOrdersButton.setBackgroundColor(getResources().getColor(R.color.orange));
        userOrdersButton.setBackgroundColor(getResources().getColor(R.color.gray));
        graphButton.setBackgroundColor(getResources().getColor(R.color.gray));
        orderListDescriptionText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        orderListDescriptionResolution.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        List<Order> marketDepthForPairAndSide = getContentProvider().getMarketDepthOrders(getContentProvider().getCurrentCurrencyPair(), orderSide);
        ordersList.setAdapter(new ExchangeOrderListViewAdapter(getContext(), marketDepthForPairAndSide, true));

        createOrdersHeader(false);
        displayGraph = false;
        myOrders = false;
    }

    private void showUserOrders() {
        this.ordersList.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        this.chart.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        marketOrdersButton.setBackgroundColor(getResources().getColor(R.color.gray));
        userOrdersButton.setBackgroundColor(getResources().getColor(R.color.orange));
        graphButton.setBackgroundColor(getResources().getColor(R.color.gray));
        orderListDescriptionText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        orderListDescriptionResolution.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        this.currentUserOrders = getContentProvider().getAccountOrders(getContentProvider().getCurrentCurrencyPair(), orderSide);
        ordersList.setAdapter(new ExchangeOrderListViewAdapter(getContext(), currentUserOrders, false));

        createOrdersHeader(true);
        displayGraph = false;
        myOrders = true;
    }

    private void showGraph() {
        this.ordersList.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        this.chart.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        marketOrdersButton.setBackgroundColor(getResources().getColor(R.color.gray));
        userOrdersButton.setBackgroundColor(getResources().getColor(R.color.gray));
        graphButton.setBackgroundColor(getResources().getColor(R.color.orange));
        orderListDescriptionText.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        orderListDescriptionResolution.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        resolutionMonth.setBackgroundColor(getResources().getColor(
            getContentProvider().getGraphResolution().equals("1M") ? R.color.orange : R.color.gray
        ));
        resolutionHour.setBackgroundColor(getResources().getColor(
            getContentProvider().getGraphResolution().equals("1H") ? R.color.orange : R.color.gray
        ));
        resolutionDay.setBackgroundColor(getResources().getColor(
            getContentProvider().getGraphResolution().equals("1D") ? R.color.orange : R.color.gray
        ));

        BarsArrays bars = getContentProvider().getHistoryBars(getContentProvider().getCurrentCurrencyPair());
        ArrayList<CandleEntry> values = new ArrayList<>();
        for (int index = 0; index < bars.getT().size(); index++) {
            values.add(new CandleEntry(
                // Datetime is accessed using x axis value formatter
                index,
                bars.getH().get(index).floatValue(),
                bars.getL().get(index).floatValue(),
                bars.getO().get(index).floatValue(),
                bars.getC().get(index).floatValue(),
                getResources().getDrawable(R.drawable.up_arrow)
            ));
        }
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float index) {
                return DateFormatter.getStringFromDate(
                    new Date((long) bars.getT().get((int) index).floatValue()),
                    DateFormatter.FORMAT_DD_MM_YYYY_HH_MM
                );
            }
        });
        CandleDataSet set = new CandleDataSet(values, "Data Set");
        set.setDrawIcons(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.rgb(80, 80, 80));
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(3f);
        set.setDecreasingColor(Color.RED);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(Color.rgb(122, 242, 84));
        set.setIncreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
        set.setNeutralColor(Color.BLUE);
        chart.setData(new CandleData(set));
        chart.invalidate();

        displayGraph = true;
        myOrders = false;
    }

    private void setLogo(String coin, ImageView logo) {
        logo.setImageDrawable(CoinHelper.getDrawableForCoin(getContext(), coin));
    }

    private void updateOnCurrencyPairChange(String pair, boolean hasData) {
        getContentProvider().setCurrentCurrencyPair(pair);
        if (!hasData) {
            // Currency pair was changed, so we have to force reset the price
            priceEdit.setText("");
            // Loads the new currency pair using refresher by displaying a loading dialog
            getMainActivity().getContentRefresher().switchFragment(FRAGMENT_EXCHANGE);
            //getMainActivity().getDataBeforeSwitch(FRAGMENT_EXCHANGE, null, true);
            return;
        }
        //getMainActivity().getTradingApiHelper().marketDepthForPair(MainActivity.asyncTaskId++,pair);
        //getMainActivity().showProgressDialog("Načítavám dáta");

        String[] currencies = pair.split("_");
        firstCurrencyText.setText(currencies[0]);
        secondCurrencyText.setText(currencies[1]);
        setLogo(currencies[0], firstCurrencyLogo);
        setLogo(currencies[1], secondCurrencyLogo);
        amountCoin.setText(currencies[0]);
        priceCoin.setText(currencies[1]);
        feeCoin.setText(currencies[1]);
        sumCoin.setText(currencies[1]);
        switch (orderSide) {
            case SELL:
                switchToSellMode(false);
                break;
            case BUY:
                switchToBuyMode(false);
                break;
            default:
                throw new RuntimeException("Unexpected order side");
        }
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

    public void setFrameSizeWhenKeyboardShown(boolean isExpanded) {
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

    @Override
    public void refreshFragment() {
        updateOnCurrencyPairChange(getContentProvider().getCurrentCurrencyPair(), true);
        Log.d(ExchangeFragment.class.getSimpleName(), "Refreshed fragment");
    }
}
