package smoothsail.operations.git.api

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.RebaseCommand
import org.springframework.stereotype.Component
import java.io.File

@Component
// FIXME THIS CLASS NEEDS TO BE ATOMIC!!!
class GithubJGitApi: GithubApi {

  private val cloneBaseLocation by lazy {
    val cloneFile = File("./testgitshit")
    cloneFile.mkdirs()
    return@lazy cloneFile
  }

  override fun rebaseAndPush(repository: String, origin: String, target: String, newBranchName: String): String {
    val clonedGitRepo = Git.cloneRepository()
        .setURI(repository)
        .setDirectory(cloneBaseLocation)
        .setBranchesToClone(listOf(origin, target))
        .setBranch(origin)
        .call()

    val checkedOutNewBranch = clonedGitRepo.checkout()
            .setCreateBranch(true)
            .setStartPoint(origin)
            .setName(newBranchName)
            .call()

    val pullRebaseResult = clonedGitRepo.pull()
            .setRemoteBranchName(target)
            .setRebase(true)
            .call()

    if (pullRebaseResult.isSuccessful) {
      return clonedGitRepo.repository
              .findRef(newBranchName).objectId.name
    } else {
      throw Exception() //FIXME throw something meaningful
    }
  }

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