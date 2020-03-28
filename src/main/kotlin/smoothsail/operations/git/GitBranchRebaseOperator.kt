package smoothsail.operations.git

import org.springframework.stereotype.Component
import smoothsail.operations.git.model.GitBranchDetails

@Component
class GitBranchRebaseOperator {

  fun operate(origin: String, target: String, mergeBranchName: String): GitBranchDetails {
    //todo merge the two branches in a new branch and upload it
    TODO()
  }

}