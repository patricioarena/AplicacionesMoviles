package com.example.jyc

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_account_settings.*


class AccountSettingsFragment : Fragment() {

    private var listener: OnFragmentActionsListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save_info_profile_btn.setOnClickListener(){
            listener?.onClickFragmentButtonAcept()
        }

        close_info_profile_btn.setOnClickListener() {
            listener?.onClickFragmentButtonCancel()
//            ProfileActivity?.getSupportFragmentManager()?.popBackStack()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentActionsListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}




