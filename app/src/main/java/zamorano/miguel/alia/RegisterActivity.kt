package zamorano.miguel.alia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    // Referencia a la base de datos firebase
    val databaseReference = FirebaseDatabase.getInstance().reference
    // Referncia a firestore de firebase
    val storageReference = FirebaseStorage.getInstance().reference
    // Referencia al usuario en firebase
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    val idUsuario = currentFirebaseUser!!.uid

    // Valores extras
    lateinit var carrera: String
    lateinit var storageRef: StorageReference
    lateinit var downloadUri: Uri

    companion object {
        private val PICK_IMAGE_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        storageRef = FirebaseStorage.getInstance().getReference("image_upload")

        spinnerDeclarations()

        userImage.setOnClickListener {
            launchGallery()
        }

        btn_listo.setOnClickListener {
            saveOnDatabase()
        }

    }

    /**
     * launchGallery abre la galeria de imagenes en la cual se podrá seleccionar una imagen
     * y se le asignará al usuario.
     */
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE)
    }

    /**
     * onActivityResult obtendrá la imágen seleccionada del método launchGallery
     * y la subirá a Firebase para obtener un uri, el cual será agregado al usuario
     * cuando este se suba a la base de datos.
     */
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_CODE) {
            val uploadTask = storageRef.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask {
                task ->
                if(!task.isSuccessful) {
                    Toast.makeText(this, "Imágen no se subió", Toast.LENGTH_LONG).show()
                }
                storageRef.downloadUrl
            }.addOnCompleteListener {
                task ->
                if(task.isSuccessful) {
                    downloadUri = task.result!!
                    Picasso.get().load(downloadUri).into(userImage);
                    Toast.makeText(this, "Imágen se subió con éxito", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Método saveOnDatabase crea un usuario nuevo, obtiene el uid del usuario
     * que se encuentra registrando y lo guarda en Firebase.
     */
    private fun saveOnDatabase() {
        val nombre = register_nombre.text
        val edad = register_edad.text
        val valores = register_valores.text
        val esConductora = esConductora.isChecked

        if(nombre.isNotEmpty() && edad.isNotEmpty() && valores.isNotEmpty()) {
            /**
             * Se crea un usuario con los valores obtenidos a lo largo
             * del activity.
             * Se agrega un campo vacio a rutas para que el campo sea reconocido al momento
             * de agregar rutas
             */
            val usuario = User(
                nombre.toString(),
                edad.toString(),
                carrera,
                valores.toString(),
                esConductora,
                downloadUri.toString(),
                puntuacion = 0.0F,
                rutas = arrayListOf("")
            )

            // Se guarda al usuario en la base de datos de Firebase
            databaseReference
                .child("users")
                .child(idUsuario)
                .setValue(usuario)

            showMenu()

        } else {
            showAlert()
        }

    }

    /**
     * Método ShowAlert construirá y mostrará un error en caso que existan
     * campos vacíos.
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se permiten campos vacíos.")
        builder.setPositiveButton("Acepter", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * ShowMenu se encarga de hacer un intent a la pantalla de MenuViaje, esto en caso
     * que no existan errores.
     */
    private fun showMenu() {
        val menuViajeIntent: Intent = Intent(this, MenuViajeActivity::class.java)
        startActivity(menuViajeIntent)
    }

    /**
     * El método SpinnerDeclarations crea un ArrayAdapter usando el arreglo de Strings
     * en el drawable de Strings para crear el Spinner con las diferentes opciones.
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
     * Método onItemSelected iguala la variable carrera al valor seleccionado
     * en el Spinner del activity.
     * @param parent parent del spinner.
     * @param view vista del spinner.
     * @param position posición del elemento seleccionado
     * @param id id del spinner.
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        carrera = if(position != 0) {
            parent?.getItemAtPosition(position).toString()
        } else {
            parent?.getItemAtPosition(1).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { TODO("Not yet implemented") }
}
