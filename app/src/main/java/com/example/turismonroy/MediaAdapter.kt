package com.example.turismonroy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class MediaAdapter(
    private var mediaFiles: List<File>,
    private val context: Context,
    private val isGalleryMode: Boolean = false
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    // ViewHolder que representa cada elemento de la lista
    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.media_image_view)
    }

    // Crea la vista para cada elemento del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        // Usa una disposición diferente si es el modo galería (diseño principal)
        val layout = if (isGalleryMode) R.layout.item_media else R.layout.item_media
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MediaViewHolder(view)
    }

    // Enlaza los datos con cada vista
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val file = mediaFiles[position]

        // Genera un URI para acceder al archivo usando FileProvider
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "com.example.turismonroy.fileprovider",
            file
        )

        // Cargar la imagen o video en el ImageView usando Glide
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .into(holder.imageView)

        // Configura un clic para abrir la imagen o video en el visor del dispositivo
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, if (file.extension == "mp4") "video/*" else "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(intent)
        }
    }

    // Retorna el número de elementos en la lista
    override fun getItemCount(): Int = mediaFiles.size

    // Método para actualizar la lista de archivos en el adaptador
    fun updateMediaFiles(newMediaFiles: List<File>) {
        mediaFiles = newMediaFiles
        notifyDataSetChanged() // Notifica al adaptador para refrescar la vista
    }
}
