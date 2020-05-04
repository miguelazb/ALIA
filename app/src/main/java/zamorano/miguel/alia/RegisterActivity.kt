package zamorano.miguel.alia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var register_carrera: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinnerDeclarations()

        btn_listo.setOnClickListener {
            saveOnDatabase()
            getUID()
            //showMenu()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) { TODO("Not yet implemented") }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position != 0) {
            register_carrera = parent?.getItemAtPosition(position).toString()
        } else {
            register_carrera = parent?.getItemAtPosition(1).toString()
        }
    }

    private fun spinnerDeclarations() {
        val spinner: Spinner = findViewById(R.id.majors_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.majors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }

    private fun saveOnDatabase() {
        val usuario = User(
            register_nombre.text.toString(),
            register_edad.text.toString(),
            register_carrera,
            register_valores.text.toString()
        )

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(getUID())
            .setValue(usuario)
    }

    private fun showMenu() {
        val menuViajeIntent: Intent = Intent(this, MenuViajeActivity::class.java)
        startActivity(menuViajeIntent)
    }

    private fun getUID(): String {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        Toast.makeText(this, "" + currentFirebaseUser!!.uid, Toast.LENGTH_SHORT).show()
        return currentFirebaseUser!!.uid
    }
}
