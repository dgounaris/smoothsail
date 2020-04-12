package smoothsail.operations.git.api

import org.eclipse.jgit.api.Git

interface GithubApi {
  fun pullCheckoutBranch(repository: String, branch: String): Git
}