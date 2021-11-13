package kr.co.hanbit.networkretrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.hanbit.networkretrofit.databinding.ItemRecyclerBinding

// 3. 리사이클러뷰, 아이템, 어댑터 및 뷰 홀더 정의
class CustomAdapter: RecyclerView.Adapter<Holder>() {

    var userList: Repository? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = userList?.get(position)
        holder.setUser(user)
    }

    override fun getItemCount(): Int {
        return userList?.size?: 0
    }
}

class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    fun setUser(user: RepositoryItem?){
        // 아바타 주소, 사용자 이름, 사용자 ID 정보 사용
        // 사용자 이름과 ID 세팅
        user?.let{
            binding.textName.text = user.name
            binding.textId.text = user.node_id
            Glide.with(binding.imageAvatar).load(user.owner.avatar_url).into(binding.imageAvatar)
        }
    }
}