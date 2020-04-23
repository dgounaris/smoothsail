package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus

@Component
class UpdateBuildBranchActionSelectionStrategy(
    private val startedUpdateBuildBranchAction: StartedUpdateBuildBranchAction,
    private val successUpdateBuildBranchAction: SuccessUpdateBuildBranchAction,
    private val failureUpdateBuildBranchAction: FailureUpdateBuildBranchAction,
    private val abortUpdateBuildBranchAction: AbortUpdateBuildBranchAction,
    private val nullUpdateBuildBranchAction: NullUpdateBuildBranchAction
) {

  fun select(updatedStatus: BuildBranchStatus) =
    when (updatedStatus) {
      BuildBranchStatus.STARTED -> startedUpdateBuildBranchAction
      BuildBranchStatus.SUCCESS -> successUpdateBuildBranchAction
      BuildBranchStatus.FAILURE -> failureUpdateBuildBranchAction
      BuildBranchStatus.ABORTED -> abortUpdateBuildBranchAction
      else -> nullUpdateBuildBranchAction
    }
}

interface UpdateBuildBranchAction {
  fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?)
}

@Component
class NullUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails, jobName: String?, buildNumber: Long?) {
    // do nothing
  }
}