package ru.kekulta.kotlingrid.adapter

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.kekulta.kotlingrid.fragment.ImagePagerFragment
import ru.kekulta.kotlingrid.MainActivity
import ru.kekulta.kotlingrid.R
import ru.kekulta.kotlingrid.adapter.ImageData.IMAGE_DRAWABLES
import java.util.concurrent.atomic.AtomicBoolean


/**
 * A fragment for displaying a grid of images.
 */
class GridAdapter(fragment: Fragment) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    private val requestManager: RequestManager
    private val viewHolderListener: ViewHolderListener

    /**
     * Constructs a new grid adapter for the given {@link Fragment}.
     */
    init {
        requestManager = Glide.with(fragment)
        viewHolderListener = ViewHolderListenerImpl(fragment)
    }

    /**
     * A listener that is attached to all ViewHolders to handle image loading events and clicks.
     */
    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)
        fun onItemClicked(view: View?, adapterPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_card, parent, false)
        return ImageViewHolder(view, requestManager, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int {
        return IMAGE_DRAWABLES.size
    }


    inner class ViewHolderListenerImpl(private val fragment: Fragment) : ViewHolderListener {

        private val enterTransitionStarted: AtomicBoolean = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView?, adapterPosition: Int) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainActivity.currentPosition != adapterPosition) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }

        /**
         * Handles a view click by setting the current position to the given {@code position} and
         * starting a {@link  ImagePagerFragment} which displays the image at the position.
         *
         * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
         * GridFragment's SharedElementCallback)
         * @param adapterPosition the selected view position
         */
        override fun onItemClicked(view: View?, adapterPosition: Int) {

            // Update the position.
            MainActivity.currentPosition = adapterPosition

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            (fragment.exitTransition as TransitionSet?)!!.excludeTarget(view, true)

            val transitioningView = view!!.findViewById<ImageView>(R.id.card_image)

            fragment.parentFragmentManager.commit {
                setReorderingAllowed(true)
                addSharedElement(transitioningView, transitioningView.transitionName)
                replace(
                    R.id.fragment_container, ImagePagerFragment(), ImagePagerFragment::class.java
                        .simpleName
                )
                addToBackStack(null)
            }

        }

    }

    /**
     * ViewHolder for the grid's images.
     */
    inner class ImageViewHolder(
        itemView: View,
        requestManager: RequestManager,
        viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val image: ImageView
        private val requestManager: RequestManager
        private val viewHolderListener: ViewHolderListener

        init {
            this.image = itemView.findViewById(R.id.card_image)
            this.requestManager = requestManager
            this.viewHolderListener = viewHolderListener
            itemView.findViewById<View>(R.id.card_view).setOnClickListener(this)
        }


        /**
         * Binds this view holder to the given adapter position.
         *
         * The binding will load the image into the image view, as well as set its transition name for
         * later.
         */
        fun onBind() {
            setImage()
            // Set the string value of the image resource as the unique transition name for the view.
            image.transitionName = IMAGE_DRAWABLES[adapterPosition].toString()
        }

        private fun setImage() {
            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            requestManager
                .load(IMAGE_DRAWABLES[adapterPosition])
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any,
                        target: Target<Drawable?>, isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }
                })
                .into(image)
        }


        override fun onClick(view: View?) {
            // Let the listener start the ImagePagerFragment.
            viewHolderListener.onItemClicked(view, adapterPosition)
        }

    }


}