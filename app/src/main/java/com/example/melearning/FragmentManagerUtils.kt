package com.example.melearning

import android.os.Bundle
import androidx.fragment.app.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
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

        fun checkFragmentIsAlreadyAdded(
            fragmentManager: FragmentManager,
            fragment: Fragment): Boolean
        {
            return fragmentManager.fragments.contains(fragment)
        }

        inline fun <reified T: Fragment> fragmentTag() = T::class.java.name
        inline fun <reified T: Fragment> backStackTag() = "${fragmentTag<T>()}:state"
        fun fragmentTag(fragment: Fragment) = fragment::class.java.name
        fun backStackTag(fragment: Fragment) = "${fragmentTag(fragment)}:state"

        inline fun <reified T: Fragment> showFragmentAnimated(fragmentManager: FragmentManager)
        {
            showFragment<T>(fragmentManager,
                R.anim.move_in_left, R.anim.fade_out,
                R.anim.empty, R.anim.move_out_right)
        }

        inline fun <reified T: Fragment> showFragment(
            fragmentManager: FragmentManager,
            curEnter: Int = R.anim.move_in_left,
            prevExit: Int = R.anim.move_out_left,
            prevEnter: Int = R.anim.move_in_right,
            curExit: Int = R.anim.move_out_right,
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

        inline fun addFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            callInTransaction: FragmentTransaction.() -> Unit = {},
            container: Int = R.id.fragment_container_view)
        {
            if(checkFragmentIsAlreadyAdded(fragmentManager, fragment)) {
                println("fragment class already exist in manager")
                return
            }

            fragmentManager.commit(true) {
                setReorderingAllowed(true)
                callInTransaction()
                val className = fragmentTag(fragment)
                add(container, fragment, className)
                addToBackStack(backStackTag(fragment))
            }
        }

        inline fun <reified T: Fragment> addFragment(
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
                add<T>(container, className, bundle)
                addToBackStack(backStackTag<T>())
            }
        }

        inline fun removeFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            callInTransaction: FragmentTransaction.() -> Unit = {})
        {
            fragmentManager.commit(true) {
                setReorderingAllowed(true)
                callInTransaction()
                remove(fragment)
            }
            fragmentManager.popBackStack()
        }

    }
}

