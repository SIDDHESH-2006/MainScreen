package com.example.myscreen
import androidx.appcompat.app.AlertDialog


import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var income = 1000.0
    private var spending = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val incomeText = findViewById<TextView>(R.id.incomeText)
        val spendingText = findViewById<TextView>(R.id.spendingText)
        val balanceText = findViewById<TextView>(R.id.balanceText)
        val transactionList = findViewById<LinearLayout>(R.id.transactionList)
        val addButton = findViewById<Button>(R.id.addTransactionBtn)

        // Initial setup
        incomeText.text = "Income\n₹%.2f".format(income)
        spendingText.text = "Spending\n₹%.2f".format(spending)
        balanceText.text = "Balance : ₹%.2f".format(income - spending)

        addButton.setOnClickListener {
            // Show custom input dialog
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Expense")
            builder.setView(dialogView)

            val itemInput = dialogView.findViewById<EditText>(R.id.input_item)
            val amountInput = dialogView.findViewById<EditText>(R.id.input_amount)
            builder.setPositiveButton("Add") { _, _ ->
                val item = itemInput.text.toString().ifBlank { "Unnamed" }
                val amountText = amountInput.text.toString()
                val amount = amountText.toDoubleOrNull() ?: 0.0

                if (amount > (income - spending)) {
                    // Show warning popup
                    AlertDialog.Builder(this)
                        .setTitle("Insufficient Balance")
                        .setMessage("You don't have enough balance to add this expense.")
                        .setPositiveButton("OK", null)
                        .show()
                    return@setPositiveButton  // Exit early, don't add expense
                }

                val transactionView = layoutInflater.inflate(R.layout.transaction_item, null)

                transactionView.findViewById<TextView>(R.id.text1).text = item
                transactionView.findViewById<TextView>(R.id.text2).text = "Manual Entry"
                transactionView.findViewById<TextView>(R.id.text3).text = "₹%.2f".format(amount)

                transactionList.addView(transactionView)

                spending += amount
                spendingText.text = "Spending\n₹%.2f".format(spending)
                balanceText.text = "Balance : ₹%.2f".format(income - spending)
            }


            builder.setNegativeButton("Cancel", null)
            builder.show()
        }

    }
}
