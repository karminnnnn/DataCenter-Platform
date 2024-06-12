import service from '@/utils/request'

export const useOdsApi = (datatableId: number) => {
	return service.get('/data-integrate/ods/' + datatableId)
}

export const useOdsSubmitApi = (dataForm: any) => {
	if (dataForm.datatableId) {
		return service.put('/data-integrate/ods', dataForm)
	} else {
		return service.post('/data-integrate/ods', dataForm)
	}
}

export const getColumnInfoApi = (tableName: any) => {
	return service.get('/data-integrate/ods/' + tableName + '/column-info')
}

export const getTableDataApi = (tableName: any) => {
	return service.get('/data-integrate/ods/' + tableName + '/table-data')
}


// 以下为新增
// 数据表字段部分
export const useOdsColumnInfoApi = (fieldName: any, datatableId: number) => {
	return service.get('/data-integrate/ods/column-info/' + datatableId + '/' + fieldName)
}

export const useOdsColumnInfoSubmitApi = (dataForm: any, oldfieldname:any) => {
	if (dataForm.oldfieldname) {
		return service.put('/data-integrate/ods/' + oldfieldname + '/column-info', dataForm)
	} else {
		return service.post('/data-integrate/ods/column-info', dataForm)
	}
}

export const deleteOdsColumnInfoApi = (datatableId: number, fieldName: any) => {
	return service.delete('/data-integrate/ods/column-info/' + datatableId + '/' + fieldName)
}

// 数据表数据部分
export const useOdsTableDataPutApi = (dataForm: any) => {
	return service.put('/data-integrate/ods/tabledata', dataForm)
}

export const useOdsTableDataPostApi = (dataForm: any) => {
	return service.post('/data-integrate/ods/tabledata', dataForm)
}

export const useOdsTableDataHeaderInfoApi = (datatableId: number) => {
	return service.get('/data-integrate/ods/tabledata/headers/' + datatableId)
}