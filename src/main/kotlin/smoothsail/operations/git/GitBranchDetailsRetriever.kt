package smoothsail.operations.git

import org.springframework.stereotype.Component
import smoothsail.operations.git.api.GithubApi
import smoothsail.operations.git.model.GitBranchDetails

@Component
class GitBranchDetailsRetriever(
    private val githubApi: GithubApi
) {

  fun retrieve(repository: String, branch: String) =
      GitBranchDetails(
          branch,
          githubApi.pullCheckoutBranch(repository, branch)
      )

}