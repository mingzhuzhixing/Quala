package com.kkkl.cowieu.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * ClassName: QualaListBean
 * Description: home页数据类型
 *
 * @author zhaowei
 * @package_name  com.kkkl.cowieu.bean
 * @date 2023/12/19 10:44
 */
class QualaListBean : Parcelable {
    var type: String? = null
    var contacts: List<ContactsBean>? = null
    var tag: String? = null
    var tags: List<String>? = null
    var address: String? = null
    var top = 0
    var company: String? = null
    var id: String? = null
    var jobType: String? = null
    var salary: String? = null
    var title: String? = null
    var descriptions: List<DescriptionsBean>? = null

    constructor()
    protected constructor(`in`: Parcel) {
        type = `in`.readString()
        contacts = `in`.createTypedArrayList(ContactsBean.CREATOR)
        tag = `in`.readString()
        tags = `in`.createStringArrayList()
        address = `in`.readString()
        top = `in`.readInt()
        company = `in`.readString()
        id = `in`.readString()
        jobType = `in`.readString()
        salary = `in`.readString()
        title = `in`.readString()
        descriptions = `in`.createTypedArrayList(DescriptionsBean.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(type)
        parcel.writeTypedList(contacts)
        parcel.writeString(tag)
        parcel.writeStringList(tags)
        parcel.writeString(address)
        parcel.writeInt(top)
        parcel.writeString(company)
        parcel.writeString(id)
        parcel.writeString(jobType)
        parcel.writeString(salary)
        parcel.writeString(title)
        parcel.writeTypedList(descriptions)
    }

    class DescriptionsBean : Parcelable {
        var title: String? = null
        var contents: List<String>? = null

        constructor()

        protected constructor(`in`: Parcel) {
            title = `in`.readString()
            contents = `in`.createStringArrayList()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(title)
            parcel.writeStringList(contents)
        }

        companion object CREATOR : Parcelable.Creator<DescriptionsBean> {
            override fun createFromParcel(parcel: Parcel): DescriptionsBean {
                return DescriptionsBean(parcel)
            }

            override fun newArray(size: Int): Array<DescriptionsBean?> {
                return arrayOfNulls(size)
            }
        }
    }

    class ContactsBean : Parcelable {
        var id = 0
        var url: String? = null
        var rate = 0
        var text: String? = null

        constructor()
        protected constructor(`in`: Parcel) {
            id = `in`.readInt()
            url = `in`.readString()
            rate = `in`.readInt()
            text = `in`.readString()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(id)
            dest.writeString(url)
            dest.writeInt(rate)
            dest.writeString(text)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ContactsBean> {
            override fun createFromParcel(`in`: Parcel): ContactsBean {
                return ContactsBean(`in`)
            }

            override fun newArray(size: Int): Array<ContactsBean?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object CREATOR : Parcelable.Creator<QualaListBean?> {
        override fun createFromParcel(`in`: Parcel): QualaListBean {
            return QualaListBean(`in`)
        }

        override fun newArray(size: Int): Array<QualaListBean?> {
            return arrayOfNulls(size)
        }
    }
}