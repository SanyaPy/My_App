package com.example.expensetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText expenseEditText;
    private Spinner categorySpinner;
    private Button addExpenseButton;
    private Button clearExpensesButton;
    private ListView expensesListView;

    private TextView totalExpenseTextView;

    private ArrayList<String> expensesList;
    private ArrayAdapter<String> expensesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseEditText = findViewById(R.id.expenseEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        expensesListView = findViewById(R.id.expensesListView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);


        expensesList = new ArrayList<>();
        expensesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expensesList);
        expensesListView.setAdapter(expensesAdapter);
        clearExpensesButton = findViewById(R.id.clearExpensesButton);
        clearExpensesButton.setEnabled(!expensesList.isEmpty());


        String[] expenseCategories = getResources().getStringArray(R.array.expense_categories);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseCategories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expense = expenseEditText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();

                if (expense.isEmpty()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Будь ласка, введіть суму витрат", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return;
                }

                String expenseItem = "Категорія: " + category + ", Витрати: $" + expense;
                expensesList.add(expenseItem);
                expensesAdapter.notifyDataSetChanged();
                clearExpensesButton.setEnabled(true);

                expenseEditText.setText("");
                updateTotalExpense();
            }
        });


        clearExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClearExpenses();
            }
        });

        expensesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmDeleteExpense(position);
            return true;
        });

        updateTotalExpense();
    }

    private void updateTotalExpense() {
        double totalExpense = 0;
        for (String expense : expensesList) {
            String expenseAmount = expense.substring(expense.lastIndexOf("$") + 1);
            totalExpense += Double.parseDouble(expenseAmount);
        }
        totalExpenseTextView.setText("Загальні витрати: $" + totalExpense);
    }
    private void clearExpenses() {
        expensesList.clear();
        expensesAdapter.notifyDataSetChanged();
        updateTotalExpense();
        clearExpensesButton.setEnabled(!expensesList.isEmpty());
    }
    private void confirmClearExpenses() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Підтвердіть дію")
                .setMessage("Ви впевнені, що хочете очистити всі витрати?")
                .setPositiveButton("Очистити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearExpenses();
                    }
                })
                .setNegativeButton("Скасувати", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void confirmDeleteExpense(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Підтвердіть видалення")
                .setMessage("Ви впевнені, що хочете видалити цю витрату" +
                        "" +
                        "?")
                .setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expensesList.remove(position);
                        expensesAdapter.notifyDataSetChanged();
                        updateTotalExpense();
                    }
                })
                .setNegativeButton("Скасувати", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();


    }};
