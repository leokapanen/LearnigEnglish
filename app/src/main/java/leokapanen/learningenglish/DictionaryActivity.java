package leokapanen.learningenglish;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import org.azeff.makeup.Makeup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import datamodel.DicRecord;
import db.DicDB;
import uiview.SearchField;


public class DictionaryActivity extends AppCompatActivity implements
        SearchField.OnStateChanged,
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<DicRecord>> {

    // Injects views

    @Bind(R.id.search_field)
    SearchField searchField;

    @Bind({R.id.studying_1, R.id.studying_2, R.id.studying_3, R.id.studying_4})
    TextView[] studyingIndicator;

    @Bind({R.id.studied_1, R.id.studied_2, R.id.studied_3, R.id.studied_4})
    TextView[] studiedIndicator;

    @Bind(R.id.not_found_tv)
    TextView notFoundTV;

    @Bind(R.id.list)
    ListView list;

    @Bind(R.id.start_btn)
    Button startBtn;

    private static final int MIN_CHECKED_WORDS_LIMIT = 2;

    private static final int DATA_LOADER_ID = 1;
    private static final int SEARCH_LOADER_ID = 2;

    private List<DicRecord> allDicRecords = new ArrayList<>(); // all dictionary records
    private List<DicRecord> currentDicRecords = new ArrayList<>();
    private Map<Integer, DicRecord> activeRecords = new TreeMap<>(); // <id, record>,
    // map of records which were selected by user

    private int studiedValue = 0;

    private ProgressDialog progressDialog;
    private AlertDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ButterKnife.bind(this);
        searchField.setListener(this);

        // Init toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getLoaderManager().initLoader(DATA_LOADER_ID, null, this);
        getLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (allDicRecords.size() == 0) {

            showLoadingDialog();

            Loader<List<DicRecord>> dataLoader = getLoaderManager().getLoader(DATA_LOADER_ID);
            dataLoader.forceLoad();
        }
    }

    private synchronized void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
        }
    }

    private synchronized void closeDialogs() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (errorDialog != null) {
            errorDialog.dismiss();
            errorDialog = null;
        }
    }

    private synchronized void showErrorDialog() {
        if (errorDialog != null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(R.string.loading_error);
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                errorDialog = null;
                showLoadingDialog();
                getLoaderManager().getLoader(DATA_LOADER_ID).forceLoad();
            }
        });

        errorDialog = builder.create();
        errorDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void updateUI() {
        int studyingValue = 100;

        if (currentDicRecords.size() > 0) {
            notFoundTV.setVisibility(View.GONE);
        } else {
            notFoundTV.setVisibility(View.VISIBLE);
        }

        list.setAdapter(new DicListAdapter<>(getApplicationContext(), currentDicRecords));
        list.setOnItemClickListener(this);

        updateIndicators(allDicRecords.size() - studiedValue, studiedValue);
        updateStartBtn();
    }

    private void updateIndicators(int studyingValue, int studiedValue) {
        // clearing indicators
        for (TextView indItem : studyingIndicator) {
            indItem.setText("0");
        }
        for (TextView indItem : studiedIndicator) {
            indItem.setText("0");
        }

        // updating indicators
        for (int i = 0; i < studyingIndicator.length; i++) {
            if (studyingValue > 0) {
                studyingIndicator[i].setText(Integer.toString(studyingValue % 10));
                studyingValue /= 10;
            }
        }

        for (int i = 0; i < studiedIndicator.length; i++) {
            if (studiedValue > 0) {
                studiedIndicator[i].setText(Integer.toString(studiedValue % 10));
                studiedValue /= 10;
            }
        }
    }

    private void updateStartBtn() {
        if (activeRecords.size() >= MIN_CHECKED_WORDS_LIMIT) {
            startBtn.setVisibility(View.VISIBLE);
        } else {
            startBtn.setVisibility(View.GONE);
        }
    }

    class DicListAdapter<DicRecord> extends ArrayAdapter<DicRecord> {

        final int TEXT_COLOR = getResources().getColor(R.color.text_grey);

        public DicListAdapter(Context context, List<DicRecord> records) {
            super(context, 0, records);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            final datamodel.DicRecord record = (datamodel.DicRecord) getItem(position);
            if (!record.isStudied()) {
                holder.item.setEnabled(true);
                holder.item.setChecked((activeRecords.get(record.getId()) != null) ? true : false);
            } else {
                holder.item.setChecked(true);
                holder.item.setEnabled(false);
            }

            Spannable label = Makeup.create()
                    .append(record.getSentence()).bold().color(TEXT_COLOR)
                    .append(" - ").color(TEXT_COLOR)
                    .append(record.getTranslation()).color(TEXT_COLOR)
                    .apply();

            holder.item.setText(label);

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.list_item)
            CheckedTextView item;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    // Loaders

    @Override
    public Loader<List<DicRecord>> onCreateLoader(int id, Bundle args) {
        Loader<List<DicRecord>> loader = null;
        switch (id) {
            case DATA_LOADER_ID:
                loader = new DataLoader(this);
                break;

            case SEARCH_LOADER_ID:
                Bundle bundle = new Bundle();
                bundle.putString(Conf.KEY_SEARCH_STRING, searchField.getText().toString());
                loader = new SearchLoader(this, bundle);
                break;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<DicRecord>> loader, List<DicRecord> data) {
        closeDialogs();

        switch (loader.getId()) {
            case DATA_LOADER_ID:
                if (data.size() == 0) {
                    showErrorDialog();
                }

                allDicRecords.clear();
                allDicRecords.addAll(data);

                searchField.setText("");

                currentDicRecords.clear();
                currentDicRecords.addAll(data);

                studiedValue = DicDB.INSTANCE.getStudiedCount();
                break;

            case SEARCH_LOADER_ID:
                currentDicRecords.clear();
                currentDicRecords.addAll(data);
                break;
        }

        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<List<DicRecord>> loader) {
    }

    // Listeners

    @OnClick(R.id.start_btn)
    public void studyWords() {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra(Conf.KEY_DIC_RECORDS, new ArrayList<>(activeRecords.values()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        allDicRecords.clear();
        currentDicRecords.clear();
        activeRecords.clear();
    }

    // On search field change
    @Override
    public void onChange(boolean isEmpty, String text) {
        if (isEmpty || text.equals("")) {
            currentDicRecords.clear();
            currentDicRecords.addAll(allDicRecords);
            updateUI();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Conf.KEY_SEARCH_STRING, searchField.getText().toString());
            getLoaderManager().restartLoader(SEARCH_LOADER_ID, bundle, this);
            getLoaderManager().getLoader(SEARCH_LOADER_ID).forceLoad();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DicRecord dicRecord = (DicRecord) list.getAdapter().getItem(position);
        if (!dicRecord.isStudied()) {
            dicRecord.setSelected(!dicRecord.isSelected());

            if (dicRecord.isSelected()) {
                activeRecords.put(dicRecord.getId(), dicRecord);
            } else {
                activeRecords.remove(dicRecord.getId());
            }

            updateStartBtn();
        }

        ((ArrayAdapter) parent.getAdapter()).notifyDataSetChanged();
    }

}
