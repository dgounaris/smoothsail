package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.repository.BuildBranchDetailsRepository

@Component
class StartedUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
    override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
        val updated = buildBranchDetails.copy(
                status = BuildBranchStatus.STARTED
        )
        buildBranchDetailsRepository.save(updated)
    }
}