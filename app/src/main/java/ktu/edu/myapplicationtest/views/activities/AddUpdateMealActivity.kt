package ktu.edu.myapplicationtest.views.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import ktu.edu.myapplicationtest.app.MyMealsApp
import ktu.edu.myapplicationtest.databinding.ActivityAddUpdateMealBinding
import ktu.edu.myapplicationtest.databinding.DialogImageBinding
import ktu.edu.myapplicationtest.model.entities.Meal
import ktu.edu.myapplicationtest.viewmodel.MealsViewModel
import ktu.edu.myapplicationtest.viewmodel.MealsViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AddUpdateMealActivity : AppCompatActivity() {
    private lateinit var mealBinding: ActivityAddUpdateMealBinding
    private var imgPath = ""
    private lateinit var myDialog: Dialog
    private var id = 0

    private val mealVM: MealsViewModel by viewModels{
        MealsViewModelFactory((application as MyMealsApp).repoMeals)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        super.onCreate(savedInstanceState)
        mealBinding = ActivityAddUpdateMealBinding.inflate(layoutInflater)
        backButton(mealBinding.backBtn)
        imageView(mealBinding.ivAddMealPhoto)
        saveButton(mealBinding.saveMealBtn)
        setContentView(mealBinding.root)
    }
    private fun saveButton(save:Button){
        save.setOnClickListener {
            val name = mealBinding.mealNameInput.text.toString()
            val cals = mealBinding.caloriesInput.text.toString()
            val carbs = mealBinding.carbsInput.text.toString()
            val fats = mealBinding.fatsInput.text.toString()
            val prots = mealBinding.proteinsInput.text.toString()
            when{
                TextUtils.isEmpty(imgPath)-> {
                    imgPath = ""
                }
            }
            if(name.isEmpty() || cals.isEmpty() || carbs.isEmpty() || fats.isEmpty() || prots.isEmpty())
            {
                Toast.makeText(this@AddUpdateMealActivity,
                    "Visi laukai yra privalomi! ${imgPath}",
                    Toast.LENGTH_LONG).show()
            } else {
                val newMeal = Meal(id,name,cals.toInt(),carbs.toInt(),prots.toInt(),fats.toInt(),imgPath, false)
                if(id == 0){
                    mealVM.insert(newMeal)
                } else {
                    mealVM.update(newMeal)
                }
                Toast.makeText(this,"Patiekalas sėkmingai pridėtas!",Toast.LENGTH_LONG).show()
                Log.i("MealI", "new meal inserted!")
                finish()
            }
        }
    }

    private fun backButton(button: Button){
        button.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    private fun imageView(img: ImageView)
    {
        img.setOnClickListener{
            selectDialog()
            return@setOnClickListener
        }
    }

    private fun selectDialog(){
        myDialog = Dialog(this)
        val binding = DialogImageBinding.inflate(layoutInflater)
        myDialog.setContentView(binding.root)
        myDialog.show()
        binding.dialogCamera.setOnClickListener{
            Dexter.withContext(this).withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            ).withListener(object:MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    p0?.let{
                        if(p0!!.areAllPermissionsGranted()){
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent,CAMERA)
                        } else {
                            showAlertDialog()
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                    Toast.makeText(this@AddUpdateMealActivity,"Clicked on Gallery", Toast.LENGTH_SHORT).show()
                    showAlertDialog()
                }
            }).onSameThread().check()
            myDialog.dismiss()
        }
        binding.dialogGallery.setOnClickListener{
            Dexter.withContext(this).withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@AddUpdateMealActivity,"No permission granted",Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    showAlertDialog()
                }


            }).onSameThread().check()
            myDialog.dismiss()
        }
    }

    private fun showAlertDialog(){
        AlertDialog.Builder(this)
            .setMessage("No permissions granted!")
            .setPositiveButton("GO TO SETTINGS")
            {_,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL")
            {dialog,_ ->
                dialog.dismiss()
            }.show()
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){
                data?.let{
                    val img: Bitmap = data.extras!!.get("data") as Bitmap
                    mealBinding.ivAddMealPhoto.setImageBitmap(img)

                    imgPath = saveImg(img)
                }
            }
            if(requestCode == GALLERY){
                data?.let{
                    val photoUri = data.data
                    mealBinding.ivAddMealPhoto.setImageURI(photoUri)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("cancel", "cancelled")
        }
    }
    private fun saveImg(img:Bitmap):String{
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMG_DIR, Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")
        try{
            val stream = FileOutputStream(file)
            img.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }

    companion object{
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMG_DIR = "MyMealsApp"
    }


}