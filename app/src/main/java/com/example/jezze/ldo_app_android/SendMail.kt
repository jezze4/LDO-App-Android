package com.example.jezze.ldo_app_android

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.provider.ContactsContract
import android.widget.Toast
import java.io.File
import java.lang.ref.WeakReference
import java.nio.file.Path
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.sql.DataSource


class SendMail(private var context: Context,
               private var subject: String?,
               private var message: String?,
               private var fileName: String?,
               private var myFile: File?,
               private var filePath: String?) :
        AsyncTask<Void, Void, Void>() {

    //Variables
    private var contextRef = WeakReference<Context>(context)
    private var session : Session? = null
    private var email : String = "noreply_ldoapp@ldostaffingsolutions.com"

    //Progress Dialog. May or may not need
    private var progDialog : ProgressDialog? = null


    //Class Constructor
    /*fun SendMail (context: Context, subject : String, message : String){
        this.context = context
        this.subject = subject
        this.message = message
    }*/

    //MAYBE WILL RUN

    override fun onPreExecute() {
        super.onPreExecute()
        val mContext : Context? = contextRef.get()
        //Showing progress dialog while sending email
//        progDialog = ProgressDialog.show(context, "Sending Application", "Please wait...", false, false)
        Toast.makeText(mContext, "Sending Application...", Toast.LENGTH_SHORT).show()
    }

    override fun onPostExecute (_void : Void?){
        super.onPostExecute(_void)
        progDialog?.dismiss()
        val mContext : Context? = contextRef.get()
        Toast.makeText(mContext, "Application Sent!", Toast.LENGTH_LONG).show()
//        val toastText = "File Name: " + fileName +
//                "\nFile Path: " + filePath
//        Toast.makeText(mContext, toastText, Toast.LENGTH_LONG).show()
    }


    override fun doInBackground(vararg params: Void?): Void? {
        val props = Properties()

        props["mail.smtp.host"] = "smtp.zoho.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.EnableSSL.enable"] = "true"


        //Create Session
        session = Session.getDefaultInstance(props, getAuthenticator())

        try {
            //Creating MimeMessage object
            val mm = MimeMessage(session)

            //Setting sender address
            mm.setFrom(InternetAddress(Config.EMAIL))
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            //Adding subject
            mm.subject = subject

            val mmp = MimeMultipart()
            val textMBP = MimeBodyPart()

            //Adding message
            if(message != null)
                textMBP.setText(message)
            else
                textMBP.setText("Error in sent application : null")
            mmp.addBodyPart(textMBP)

            //Try getting attachment
            if(myFile != null) {

                val attMBP = MimeBodyPart()
                val file = filePath
                val source : javax.activation.DataSource = FileDataSource(myFile)

//                attMBP.dataHandler = DataHandler(source)
                attMBP.attachFile(file)
                attMBP.fileName = fileName
//                attMBP.fileName = "resume.pdf"
                mmp.addBodyPart(attMBP)
            }

            mm.setContent(mmp)
            //Sending email
            Transport.send(mm)

        } catch (e: MessagingException) {
            e.printStackTrace()
        }


        return null
    }



    private fun getAuthenticator(): Authenticator {
        return object : Authenticator () {
            override fun getPasswordAuthentication(): PasswordAuthentication? {
                return PasswordAuthentication(Config.EMAIL, Config.PASSWORD)
            }
        }
    }

}
