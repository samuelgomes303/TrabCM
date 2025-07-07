import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho.DAO.ProjectDao
import com.example.trabalho.data.ProjectEntity
import com.example.trabalho.data.UserEntity
import com.example.trabalho.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ProjetoGestaoViewModel(
    private val projectDao: ProjectDao,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _projects = MutableStateFlow<List<ProjectEntity>>(emptyList())
    val projects: StateFlow<List<ProjectEntity>> = _projects

    private val _gestores = MutableStateFlow<List<UserEntity>>(emptyList())
    val gestores: StateFlow<List<UserEntity>> = _gestores

    fun loadProjects(role: String, gestorId: String) = viewModelScope.launch {
        val lista = if (role.uppercase() == "ADMINISTRADOR") {
            projectDao.getAllProjects()
        } else {
            projectDao.getProjectsByManager(gestorId)
        }
        _projects.value = lista
    }

    fun loadGestores() = viewModelScope.launch {
        val allUsers = userRepo.getAllUsers()
        _gestores.value = allUsers.filter { it.role == "GESTOR_PROJETO" }
    }

    fun createProject(nome: String, description: String, idGestor: String, onComplete: () -> Unit) = viewModelScope.launch {
        val novoProjeto = ProjectEntity(
            id = UUID.randomUUID().toString(),
            nome = nome,
            description = description,
            idGestor = idGestor,
            startDate = System.currentTimeMillis(),
            endDate = null,
            state = "EM_PROGRESSO"
        )
        projectDao.createProject(novoProjeto)
        onComplete()
        loadProjects("ADMINISTRADOR", "")
    }

    fun updateProject(projeto: ProjectEntity, onComplete: () -> Unit) = viewModelScope.launch {
        projectDao.updateProject(projeto)
        onComplete()
        loadProjects("ADMINISTRADOR", "")
    }

    fun deleteProject(projeto: ProjectEntity, onComplete: () -> Unit) = viewModelScope.launch {
        projectDao.deleteProject(projeto)
        onComplete()
        loadProjects("ADMINISTRADOR", "")
    }
}
