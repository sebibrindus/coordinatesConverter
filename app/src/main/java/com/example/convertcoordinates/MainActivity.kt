package com.example.coordinatesconverter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDateTime



class MainActivity : AppCompatActivity() {

    // declaring and initializing variables - userInput (input that the user gives in the EditText field
    //                                      - buttonConvert (the button that makes the conversion)
    //                                      - textView (the variable for the text field that holds the conversion results and displays them to the screen)
    //                                      - buttonExport (the button that makes the export to CSV file)
    //                                      - conversionList (a list that stores all the values entered)
    //                                      - id (counter that counts the entries inputed)
    //                                      - conversionListModified (conversionList turned to String and removed the "=" between keys and values, removed "{}"
    //                                      - idUserInput (a variable used to check - if its the first line in the document does not add a line break)

    private var userInput: EditText? = null
    private var buttonConvert: Button? = null
    private var textView: TextView? = null
    private var buttonExport: Button? = null
    private var conversionList: LinkedHashMap<String, String> = LinkedHashMap()
    private var id = 1
    private var conversionListModified: String = ""
    private var idUserInput = ""


      // function used to display a toast (text message that disappeares) after exporting data to file
       fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
    {
        Toast.makeText(this, text, duration).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connecting variables to views in the page
        userInput = findViewById<EditText>(R.id.userInput)
        buttonConvert = findViewById<Button>(R.id.buttonConvert)
        textView = findViewById<TextView>(R.id.textView)
        buttonExport = findViewById<Button>(R.id.buttonExport)

        // line below resets the textview when activity is created
        textView?.text = ""
        // line below sets the textView to be scrollable, together with parameters from XML
        textView?.movementMethod = ScrollingMovementMethod()

        // setting the clickListener for the convert button
        buttonConvert?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                // declared the variable below just for cleaning the code a little bit
                var userInputText = userInput?.text.toString()

                // calculating the conversion from decimal to degreees
                val decimalFirst = userInputText.toDouble()           // takes the user input and makes it double (initially the editText is of type Editable)
                val degrees = userInputText.toDouble().toInt()        // value of degress is the integer part of the number
                val minutes = ((decimalFirst - degrees) * 60).toInt() // value of the minutes is the integer of the result from multiplying decimal part of the number inputed multiplied by 60
                val decimalSecond = (decimalFirst - degrees) * 60     // this is storing the result of the multiplication
                val seconds = ((decimalSecond - minutes) * 60)        // the decimal part from the previous multiplication is again multiplied by 60 to give the number of seconds

                // display all calculated values in a formatted way, together with the value inputed by user. seconds is rounded up to 2 decimals
                textView?.append("Coordinates in Degrees entered:\n " + userInputText + " = $degrees degrees $minutes minutes " + "%.2f".format(seconds) + " seconds\n\n")

                // variable below stores the converted value in degress, minutes and seconds
                val convertedValue = degrees.toString() + "° " + minutes.toString() + "' " + "%.2f".format(seconds) + "\""

                // if this is the first line, so id is 1, does not concatenate a line break into the string variable. Otherwise it does.
                if(id!=1)
                {
                    idUserInput = "\n" + id.toString() + ", " + userInputText
                }
                else
                {
                    idUserInput = id.toString() + ", " + userInputText
                }

                // all the calculated values are stored in a LinkedHashMap so the order of the input is preserved and the values are stored in pairs, the inputed coordinates and the converted values are linked
                conversionList[idUserInput] = convertedValue
                id+=1  // incrementing the id with every iteration, so every line of data has another id

                // replaces the "=" character in LinkedHashMap, as a result of converting to string, with a comma
                conversionListModified = conversionList.toString().replace("=",", ")

            }
        })

        // setting the ClickListener for the Export button
        buttonExport?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val currentDate = LocalDateTime.now().toString() // retrieving the current date and time to be used as a filename
                val fileName = currentDate + ".csv"              // setting the filename and extesion

                val fileOutputStream: FileOutputStream          // variable used to generate the output Stream

                // try - catch expression to try open the file and write to it the data as a String
                // the substring method removes the "{}" from the string.
                try {
                    fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                    fileOutputStream.write(conversionListModified.substring(startIndex = 1, endIndex = conversionListModified.length-1).toByteArray())
                }
                catch (e: FileNotFoundException)
                {
                    e.printStackTrace()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }

                // displays a toast when succesful
                showToast("File saved succesfully!")

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // function above inflates the top main menu, where the share button is located

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.share_menu)
        {
            var shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, conversionListModified.substring(startIndex = 1, endIndex = conversionListModified.length-1))
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        else
        {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    // function above states the action executed by the share button

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val textViewContent = textView?.text
        outState.putString("saved", textView?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val textViewRestore = savedInstanceState.getString("saved", "")
        textView?.text = textViewRestore

    }

    // the two functions above are making sure data is not erased when rotating screen

}




