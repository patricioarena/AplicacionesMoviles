package com.example.jyc

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_account_settings.*

import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.card_post.*
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_account_settings.view.*


class AccountSettingsFragment : Fragment() {

    private var listener: OnFragmentActionsListener? = null

        var calle: String? = null
        var numero: Int? = null
        var cp: Int? = null
        var localidad: String? = null
        var provincia: String? = null
        var pais: String? = null
        var piso: String? = null
        var eCalle_1: String? =null
        var eCalle_2: String? =null

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (getArguments() != null) {

            calle_user.setText(getArguments()?.getString("calle"))
            calle_numero_user.setText(getArguments()?.getString("numero"))
            cp_user.setText(getArguments()?.getString("cp"))
            ciudad_user.setText(getArguments()?.getString("localidad"))
            provincia_user.setText(getArguments()?.getString("provincia"))
            cel_user.setText(getArguments()?.getString("cel"))
            tel_user.setText(getArguments()?.getString("tel"))

            var avatar = getArguments()?.getString("avatar")
            if (!TextUtils.isEmpty(avatar)) {
                Picasso.get().load(avatar).into(profile_image_setting)
            }

        }

        save_info_profile_btn.setOnClickListener(){
            listener?.onClickFragmentButtonAcept()
        }

        close_info_profile_btn.setOnClickListener(){
            listener?.onClickFragmentButtonCancel()
//            ProfileActivity?.getSupportFragmentManager()?.popBackStack()
        }

        profile_image_setting.setOnClickListener(){
            listener?.onClickFragmentImage()
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




