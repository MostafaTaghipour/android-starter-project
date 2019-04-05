package ir.rainyday.android.common.supers


/**
 * Created by taghipour on 09/10/2017.
 */
abstract class SuperFragment : androidx.fragment.app.DialogFragment() {

    //region fragment visibility
    private var mIsVisibleToUser: Boolean = false // you can see this variable may absolutely <=> getUserVisibleHint() but it not. Currently, after many test I find that

    /**
     * This method will be called when viewpager creates fragment and when we go to this fragment background or another activity or fragment
     * NOT called when we switch between each page in ViewPager
     */
    override fun onStart() {
        super.onStart()
        if (mIsVisibleToUser) {
            onShowFragment()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mIsVisibleToUser) {
            onHideFragment()
        }
    }

    /**
     * This method will called at first time viewpager created and when we switch between each page
     * NOT called when we go to background or another activity (fragment) when we go back
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (isResumed) { // fragment have created
            if (mIsVisibleToUser) {
                onShowFragment()
            } else {
                onHideFragment()
            }
        }
    }


    open fun onShowFragment() {}
    open fun onHideFragment() {}
    //endregion

}
