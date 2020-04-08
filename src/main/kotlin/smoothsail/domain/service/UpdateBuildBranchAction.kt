package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.repository.BuildBranchDetailsRepository

@Component
class UpdateBuildBranchActionSelectionStrategy(
    private val successUpdateBuildBranchAction: SuccessUpdateBuildBranchAction,
    private val failureUpdateBuildBranchAction: FailureUpdateBuildBranchAction,
    private val nullUpdateBuildBranchAction: NullUpdateBuildBranchAction
) {

  fun select(updatedStatus: BuildBranchStatus) =
    when (updatedStatus) {
      BuildBranchStatus.SUCCESS -> successUpdateBuildBranchAction
      BuildBranchStatus.FAILURE -> failureUpdateBuildBranchAction
      else -> nullUpdateBuildBranchAction
    }
}

interface UpdateBuildBranchAction {
  fun execute(buildBranchDetails: BuildBranchDetails)
}

@Component
class SuccessUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
      val updated = buildBranchDetails.copy(
            status = BuildBranchStatus.SUCCESS
      )
      buildBranchDetailsRepository.save(updated)
  }
}

@Component
class FailureUpdateBuildBranchAction(
        private val buildBranchDetailsRepository: BuildBranchDetailsRepository
): UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
      val updated = buildBranchDetails.copy(
              status = BuildBranchStatus.FAILURE
      )
      buildBranchDetailsRepository.save(updated)
      // todo here there will be a logic to call a webhook so that jenkins aborts the next ones, and then reschedules
  }
}

@Component
class NullUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
    // do nothing
  }
}