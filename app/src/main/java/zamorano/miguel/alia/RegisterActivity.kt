package zamorano.miguel.alia

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var register_carrera: String
    lateinit var usuarioUID: String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        spinnerDeclarations()

        userImage.setOnClickListener {
            dispatchTakePictureIntent()
            onActivityResult(0, 0, null)
        }

        btn_listo.setOnClickListener {
            saveOnDatabase()
            showMenu()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) { TODO("Not yet implemented") }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        register_carrera = if(position != 0) {
            parent?.getItemAtPosition(position).toString()
        } else {
            parent?.getItemAtPosition(1).toString()
        }
    }

    /**
     * Método utilizado para obtener la imágen tomada en la cámara
     */
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            userImage.setImageBitmap(imageBitmap)
        }
    }

    /**
     * Método utilizado para abrir la camara y poder tomar fotos.
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    /**
     * Método que crea el Spinner através del arreglo de carreras creado en el
     * resource de Strings.
     */
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

    /**
     * Método encargado de guardar el usuario en la base de datos validando que
     * no haya ni un solo campo vacío.
     */
    private fun saveOnDatabase() {
        val usuario = User(
            register_nombre.text.toString(),
            register_edad.text.toString(),
            register_carrera,
            register_valores.text.toString()
        )

        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        usuarioUID = currentFirebaseUser!!.uid

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(usuarioUID)
            .setValue(usuario)
    }

    /**
     * Intent a la siguiente pantalla de MenuViaje
     */
    private fun showMenu() {
        val menuViajeIntent: Intent = Intent(this, MenuViajeActivity::class.java)
        startActivity(menuViajeIntent)
    }
}
