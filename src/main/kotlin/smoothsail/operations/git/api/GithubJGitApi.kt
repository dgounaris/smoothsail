package smoothsail.operations.git.api

import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File


@Component
class GithubJGitApi: GithubApi {

  @Value("\${git.location}")
  private lateinit var cloneBaseLocationName: String

  private val cloneBaseLocation by lazy {
    val cloneFile = File(cloneBaseLocationName)
    cloneFile.mkdirs()
    return@lazy cloneFile
  }

  @Synchronized
  override fun rebaseAndPush(repository: String, origin: String, target: String, newBranchName: String?): String {
    val clonedGitRepo = Git.cloneRepository()
        .setURI(repository)
        .setDirectory(cloneBaseLocation)
        .setBranchesToClone(listOf(origin, target))
        .setBranch(origin)
        .call()

    if (newBranchName != null) {
      val checkedOutNewBranch = clonedGitRepo.checkout()
              .setCreateBranch(true)
              .setStartPoint(origin)
              .setName(newBranchName)
              .call()
    }

    val pullRebaseResult = clonedGitRepo.rebase()
            .setUpstream(target)
            .call()

    if (pullRebaseResult.status.isSuccessful) {
      return clonedGitRepo.repository
              .findRef(newBranchName).objectId.name
    } else {
      throw Exception() //FIXME throw something meaningful
    }
  }

  @Synchronized
  override fun pullCheckoutBranch(repository: String, branch: String): String =
    Git.cloneRepository()
        .setURI(repository)
        .setDirectory(cloneBaseLocation)
        .setBranchesToClone(listOf(branch))
        .setBranch(branch)
        .call()
            .repository
            .findRef(branch).objectId.name

  @Synchronized
  override fun mergeAndPush(repository: String, origin: String, target: String): String {
    try {
      rebaseAndPush(repository, origin, target)
      val clonedGitRepo = Git.cloneRepository()
              .setURI(repository)
              .setDirectory(cloneBaseLocation)
              .setBranchesToClone(listOf(origin, target))
              .setBranch(origin)
              .call()

      val coCmd: CheckoutCommand = clonedGitRepo.checkout()
      coCmd.setName(target)
      coCmd.setCreateBranch(false) // probably not needed, just to make sure
      coCmd.call() // switch to target branch

      val mgCmd = clonedGitRepo.merge()
      mgCmd.include(clonedGitRepo.repository.findRef(origin))
      val res = mgCmd.call() // actually do the merge

      if (res.mergeStatus.isSuccessful) {
        return res.newHead.name
      } else {
        throw Exception()
      }
    } catch (e: Exception) {
      throw Exception() //FIXME throw something meaningful
    }
  }

}