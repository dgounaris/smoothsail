package smoothsail.operations.git

import org.springframework.stereotype.Component
import smoothsail.operations.git.model.GitBranchDetails

@Component
class GitBranchDetailsRetriever {

  fun retrieve(repository: String, branch: String): GitBranchDetails {
    // todo retrieve a branch if needed from upstream, and return its info
    TODO("Implementation pending")
  }

}