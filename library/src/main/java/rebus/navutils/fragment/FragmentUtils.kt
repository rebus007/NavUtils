package rebus.navutils.fragment

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import rebus.navutils.NavUtils
import rebus.navutils.NavUtilsMarker
import rebus.navutils.R

@NavUtilsMarker
class FragmentBuilder {

    internal var animationType: NavUtils.Anim = NavUtils.Anim.SYSTEM
        private set
    internal var noEnterAnimations: Boolean = false
        private set
    internal var noExitAnimations: Boolean = false
        private set
    internal var addToBackStack: Boolean = false
        private set
    internal var tag: String? = null
        private set
    internal var bundle: Bundle? = null
        private set
    internal var customAnimation: Boolean = false
        private set
    internal var enterResId: Int = 0
        private set
    internal var exitResId: Int = 0
        private set
    internal var popEnterResId: Int = 0
        private set
    internal var popExitResId: Int = 0
        private set

    fun animationType(animationType: NavUtils.Anim) {
        this.animationType = animationType
    }

    fun customAnimation(@AnimRes enterResId: Int, @AnimRes exitResId: Int, @AnimRes popEnterResId: Int, @AnimRes popExitResId: Int) {
        this.customAnimation = true
        this.enterResId = enterResId
        this.exitResId = exitResId
        this.popEnterResId = popEnterResId
        this.popExitResId = popExitResId
    }

    fun noEnterAnimations(noEnterAnimations: Boolean) {
        this.noEnterAnimations = noEnterAnimations
    }

    fun noExitAnimations(noExitAnimations: Boolean) {
        this.noExitAnimations = noExitAnimations
    }

    fun addToBackStack() {
        this.addToBackStack = true
    }

    fun tag(tag: String) {
        this.tag = tag
    }

    fun arguments(bundle: Bundle) {
        this.bundle = bundle
    }

}

class NavUtilsPushFragment(
        private val fragment: Fragment,
        private val frameId: Int,
        private val fragmentManager: FragmentManager?,
        private val fragmentBuilder: FragmentBuilder
) {

    fun add() {
        commitNewFragment(false)
    }

    fun replace() {
        commitNewFragment(true)
    }

    private fun commitNewFragment(replace: Boolean) {
        if (fragmentBuilder.bundle != null) fragment.arguments = fragmentBuilder.bundle
        val transaction = (fragmentManager ?: return).beginTransaction()
        if (fragmentBuilder.customAnimation) {
            transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else fragmentBuilder.enterResId, if (fragmentBuilder.noExitAnimations) 0 else fragmentBuilder.exitResId, if (fragmentBuilder.noEnterAnimations) 0 else fragmentBuilder.popEnterResId, if (fragmentBuilder.noExitAnimations) 0 else fragmentBuilder.popExitResId)
        } else {
            when (fragmentBuilder.animationType) {
                NavUtils.Anim.HORIZONTAL_RIGHT -> transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_left, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_left, if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_right, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_right)
                NavUtils.Anim.HORIZONTAL_LEFT -> transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_right, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_right, if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_left, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_left)
                NavUtils.Anim.VERTICAL_BOTTOM -> transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_bottom, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_bottom, if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_top, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_top)
                NavUtils.Anim.VERTICAL_TOP -> transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_top, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_top, if (fragmentBuilder.noEnterAnimations) 0 else R.anim.view_flipper_transition_in_bottom, if (fragmentBuilder.noExitAnimations) 0 else R.anim.view_flipper_transition_out_bottom)
                NavUtils.Anim.NONE -> transaction.setCustomAnimations(0, 0, 0, 0)
                NavUtils.Anim.SYSTEM -> transaction.setCustomAnimations(if (fragmentBuilder.noEnterAnimations) 0 else R.anim.fade_in, if (fragmentBuilder.noExitAnimations) 0 else R.anim.fade_out, if (fragmentBuilder.noEnterAnimations) 0 else R.anim.fade_in, if (fragmentBuilder.noExitAnimations) 0 else R.anim.fade_out)
            }
        }
        if (replace) {
            if (fragmentBuilder.tag != null) {
                transaction.replace(frameId, fragment, fragmentBuilder.tag)
            } else {
                transaction.replace(frameId, fragment)
            }
        } else {
            if (fragmentBuilder.tag != null) {
                transaction.add(frameId, fragment, fragmentBuilder.tag)
            } else {
                transaction.add(frameId, fragment)
            }
        }
        if (fragmentBuilder.addToBackStack) {
            transaction.addToBackStack(fragmentBuilder.tag)
        }
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }

}