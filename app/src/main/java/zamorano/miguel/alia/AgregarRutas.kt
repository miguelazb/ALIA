package zamorano.miguel.alia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_agregar_rutas.*

class AgregarRutas : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    // Referencia Usuario Firebase
    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
    val idUsuario = currentFirebaseUser!!.uid

    // Rerefencia a base datos Firebase
    var databaseReference = FirebaseDatabase.getInstance().reference

    // Linea de camión seleccionada
    lateinit var lineaCamion: String

    // Valores Usuario
    lateinit var nombre: String
    lateinit var edad: String
    lateinit var carrera: String
    lateinit var valores: String
    var calificacion: Float = 0.0F
    var esConductora: Boolean = true
    lateinit var url: String
    val callesArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_rutas)
        obtenDatosUsuario()
        spinnerDeclarations()

        btnRegistrar.setOnClickListener {
            saveOnDatabase()
        }

        btnMenuPerfil.setOnClickListener {
            val miPerfilActivity: Intent = Intent(this, MiPerfilActivity::class.java)
            startActivity(miPerfilActivity)
        }

        btnMenuPrincipal.setOnClickListener {
            val menuViajeIntent: Intent = Intent(this, MenuViajeActivity::class.java)
            startActivity(menuViajeIntent)
        }

    }

    /**
     * Método saveOnDatabase crea un usuario nuevo, obtiene el uid del usuario
     * que se encuentra registrando y lo guarda en Firebase.
     */
    private fun saveOnDatabase() {
        if (textViewCalle.text.isNotEmpty()) {
            // Crea un solo string con la linea de camion y la calle donde se encuentra
            val fullCalle = "$lineaCamion, ${textViewCalle.text}"

            // Si la posicion 0 de callesArray es vacio, lo elimina para ser
            // reemplazado por uno nuevo
            if (callesArray[0] == "") {
                callesArray.removeAt(0)
            }
            // Agrega la ruta al arreglo
            callesArray.add(fullCalle)

            // Se crea un usuario con los valores obtenidos a lo largo
            // del activity.
            val user = User(
                nombre,
                edad,
                carrera,
                valores,
                esConductora,
                url,
                calificacion,
                callesArray
            )

            // Se guarda al usuario en la base de datos de Firebase
            databaseReference
                .child("users")
                .child(idUsuario)
                .setValue(user)
            // Si todo sale bien
            showPerfil()
        } else {
            // Si existen errores
            showAlert()
        }

    }

    /**
     * obtenDatosUsuario trae todos los datos del usuario directamente de
     * la base de datos de Firebase.
     * Además agrega al arreglo de callesArray todas las rutas del usuario.
     */
    fun obtenDatosUsuario() {
        // Se posiciona en el nodo users de la base de datos
        databaseReference
            .child("users")
            .child(idUsuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                // Obtiene todos los datos del usuario desde firebase
                override fun onDataChange(p0: DataSnapshot) {
                    var map = p0.value as Map<*, *>
                    nombre = map["nombre"].toString()
                    edad = map["edad"].toString()
                    carrera = map["carrera"].toString()
                    valores = map["valores"].toString()
                    calificacion = map["puntuacion"].toString().toFloat()
                    url = map["url"].toString()

                    // Agrega a callesArray todas las rutas del usuario
                    val rutasArray: ArrayList<String> = map["rutas"] as ArrayList<String>
                    for (ruta in rutasArray) {
                        callesArray.add(ruta)
                    }
                }

            })
    }

    /**
     * showPErfil se encarga de hacer un intent a la pantalla de MiPerfil, esto en caso
     * que no existan errores.
     */
    private fun showPerfil() {
        val menuViajeIntent: Intent = Intent(this, MiPerfilActivity::class.java)
        startActivity(menuViajeIntent)
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
     * El método SpinnerDeclarations crea un ArrayAdapter usando el arreglo de Strings
     * en el drawable de Strings para crear el Spinner con las diferentes opciones.
     */
    private fun spinnerDeclarations() {
        val spinner: Spinner = findViewById(R.id.spinnerLineas)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.bus_array,
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
     * Método onItemSelected iguala la variable lineaCamion al valor seleccionado
     * en el Spinner del activity.
     * @param parent parent del spinner.
     * @param view vista del spinner.
     * @param position posición del elemento seleccionado
     * @param id id del spinner.
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        lineaCamion = if (position != 0) {
            parent?.getItemAtPosition(position).toString()
        } else {
            parent?.getItemAtPosition(1).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
