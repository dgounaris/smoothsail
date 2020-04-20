package smoothsail.operations.git

import org.springframework.stereotype.Component
import smoothsail.operations.git.api.GithubJGitApi
import smoothsail.operations.git.model.GitBranchDetails

@Component
class GitBranchRebaseOperator(
        private val githubApi: GithubJGitApi
) {

  fun operate(repository: String, origin: String, target: String, mergeBranchName: String) =
          GitBranchDetails(
                  repository,
                  githubApi.rebaseAndPush(repository, origin, target, mergeBranchName)
          )
}