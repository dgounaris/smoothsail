package smoothsail.domain.service

import org.junit.Test
import org.mockito.Mockito
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus

internal class SuccessUpdateBuildBranchActionTest: UpdateBuildBranchActionTest() {
    @Test
    fun `Entry properly updated on success`() {
        val original = BuildBranchDetails()
        successUpdateBuildBranchAction.execute(original, null, null)
        Mockito.verify(buildBranchDetailsRepository, Mockito.times(1)).save(original.copy(status = BuildBranchStatus.SUCCESS))
    }
}