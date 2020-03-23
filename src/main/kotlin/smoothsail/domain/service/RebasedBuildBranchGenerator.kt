package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails

@Component
class RebasedBuildBranchGenerator {
  fun generate(originBranch: String, latestBuildBranchDetails: BuildBranchDetails): String {
    TODO()
  }
}