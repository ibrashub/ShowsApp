package infinuma.android.shows.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinuma.android.shows.R

class ShowsViewModel : ViewModel() {


    private val shows = listOf(
        Show(
            1,
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show(
            2,
            "Stranger Things",
            "In a small town where everyone knows everyone, a peculiar incident starts a chain of events that leads to a child's disappearance, which begins to tear at the fabric of an otherwise-peaceful community. Dark government agencies and seemingly malevolent supernatural forces converge on the town, while a few of the locals begin to understand that more is going on than meets the eye.",
            R.drawable.ic_stranger_things
        ),
        Show(
            3,
            "Krv Nije Voda",
            "The series is inspired by everyday life stories that affect many families, such as the disappearance of a family member, falling into the trap of accumulated debts, the sudden collapse of a marriage due to a cheating spouse, fighting for the custody of a child, gambling addiction or problems with unruly children. Each episode in documentary form follows a particular family and the shocking story that happened to them, while the main actors and the locations where the scenes were filmed are real, which adds even more to the authenticity of the stories that always try to stay within four walls.",
            R.drawable.krv_nije_voda
        )
    )

    private val _showsLiveData: MutableLiveData<List<Show>> = MutableLiveData()
    val showsLiveData: LiveData<List<Show>> = _showsLiveData

init {
    _showsLiveData.value = shows
}

}