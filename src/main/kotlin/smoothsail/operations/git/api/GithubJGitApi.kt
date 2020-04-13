package smoothsail.operations.git.api

import org.eclipse.jgit.api.Git
import org.springframework.stereotype.Component
import java.io.File

@Component
class GithubJGitApi: GithubApi {

  private val cloneBaseLocation by lazy {
    val cloneFile = File("./testgitshit")
    cloneFile.mkdirs()
    return@lazy cloneFile
  }

  // FIXME THIS NEEDS TO BE AN ATOMIC OPERATION!!!!
  override fun pullCheckoutBranch(repository: String, branch: String): String =
    Git.cloneRepository()
        .setURI(repository)
        .setDirectory(cloneBaseLocation)
        .setBranchesToClone(listOf(branch))
        .setBranch(branch)
        .call()
            .repository
            .findRef(branch).objectId.name

}