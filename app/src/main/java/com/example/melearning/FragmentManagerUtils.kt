package com.example.melearning

import android.os.Bundle
import androidx.fragment.app.*

class FragmentManagerUtils {
    companion object {
        inline fun <reified T: Fragment>
                checkClassCurrentFragment(fragmentManager: FragmentManager): Boolean
        {
            if(fragmentManager.fragments.isNotEmpty()) {
                if(fragmentManager.fragments.first() is T) {
                    return true
                }
            }

            return false
        }

        inline fun <reified T: Fragment>
                checkFragmentIsAlreadyAdded(fragmentManager: FragmentManager): Boolean
        {
            for(fragment in fragmentManager.fragments) {
                if(fragment is T) return true
            }

            val backStackName = backStackTag<T>()
            for (num in 0 until fragmentManager.backStackEntryCount) {
                val backStack = fragmentManager.getBackStackEntryAt(num)
                if (backStack.name == backStackName) return true
            }

            return false
        }

        inline fun <reified T: Fragment> fragmentTag() = T::class.java.name
        inline fun <reified T: Fragment> backStackTag() = "${fragmentTag<T>()}:state"

        inline fun <reified T: Fragment> showFragmentAnimated(fragmentManager: FragmentManager)
        {
            showFragment<T>(fragmentManager,
                R.anim.enter, R.anim.fade_out,
                R.anim.empty, R.anim.pop_exit)
        }

        inline fun <reified T: Fragment> showFragment(
            fragmentManager: FragmentManager,
            curEnter: Int = R.anim.enter,
            prevExit: Int = R.anim.exit,
            prevEnter: Int = R.anim.pop_enter,
            curExit: Int = R.anim.pop_exit,
            container: Int = R.id.fragment_container_view)
        {
            if(checkFragmentIsAlreadyAdded<T>(fragmentManager)) return

            fragmentManager.commit(true) {
                setReorderingAllowed(true)
                setCustomAnimations(curEnter, prevExit, prevEnter, curExit)

                val className = fragmentTag<T>()
                replace<T>(container, className)
                addToBackStack(backStackTag<T>())
            }
        }
        inline fun <reified T: Fragment> showFragment(
            fragmentManager: FragmentManager,
            callInTransaction: FragmentTransaction.() -> Unit,
            bundle: Bundle? = null,
            container: Int = R.id.fragment_container_view)
        {
            if(checkFragmentIsAlreadyAdded<T>(fragmentManager)) {
                println("fragment class already exist in manager")
                return
            }

            fragmentManager.commit(true) {
                setReorderingAllowed(true)
                callInTransaction()
                val className = fragmentTag<T>()
                replace<T>(container, className, bundle)
                addToBackStack(backStackTag<T>())
            }
        }

    }
}

